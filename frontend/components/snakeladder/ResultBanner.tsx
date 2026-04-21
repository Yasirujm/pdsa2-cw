'use client';

import { BadgeCheck, Target, Scale } from 'lucide-react';

type Props = {
  result: 'WIN' | 'LOSE' | 'DRAW' | null;
  correctAnswer?: number;
};

export default function ResultBanner({ result, correctAnswer }: Props) {
  if (!result) return null;

  if (result === 'WIN') {
    return (
      <div className="mt-5 rounded-[22px] border-4 border-[#1f2937] bg-[#dff3e4] p-5 shadow-[6px_6px_0_#1f2937]">
        <div className="flex items-center justify-between gap-4">
          <div>
            <div className="text-xs font-black uppercase tracking-[0.22em] text-[#355070]">
              Result
            </div>
            <h3 className="mt-2 text-2xl font-black text-[#1f2937]">
              Correct Answer
            </h3>
            <p className="mt-2 text-sm font-medium text-[#374151]">
              Great job. You selected the minimum dice throw count.
            </p>
          </div>
          <div className="rounded-full border-4 border-[#1f2937] bg-white p-3">
            <BadgeCheck size={28} />
          </div>
        </div>
      </div>
    );
  }

  if (result === 'LOSE') {
    return (
      <div className="mt-5 rounded-[22px] border-4 border-[#1f2937] bg-[#ffd6d6] p-5 shadow-[6px_6px_0_#1f2937]">
        <div className="flex items-center justify-between gap-4">
          <div>
            <div className="text-xs font-black uppercase tracking-[0.22em] text-[#355070]">
              Result
            </div>
            <h3 className="mt-2 text-2xl font-black text-[#1f2937]">
              Wrong Answer
            </h3>
            <p className="mt-2 text-sm font-medium text-[#374151]">
              Correct answer:{' '}
              <span className="font-black text-[#1f2937]">{correctAnswer}</span>
            </p>
          </div>
          <div className="rounded-full border-4 border-[#1f2937] bg-white p-3">
            <Target size={28} />
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="mt-5 rounded-[22px] border-4 border-[#1f2937] bg-[#e0e7ff] p-5 shadow-[6px_6px_0_#1f2937]">
      <div className="flex items-center justify-between gap-4">
        <div>
          <div className="text-xs font-black uppercase tracking-[0.22em] text-[#355070]">
            Result
          </div>
          <h3 className="mt-2 text-2xl font-black text-[#1f2937]">
            Draw
          </h3>
          <p className="mt-2 text-sm font-medium text-[#374151]">
            This round ended in a draw.
          </p>
        </div>
        <div className="rounded-full border-4 border-[#1f2937] bg-white p-3">
          <Scale size={28} />
        </div>
      </div>
    </div>
  );
}