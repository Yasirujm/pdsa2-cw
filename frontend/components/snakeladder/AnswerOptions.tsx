'use client';

import { BadgeCheck, Ticket } from 'lucide-react';

type Props = {
  options: number[];
  selected: number | null;
  onSelect: (value: number) => void;
};

export default function AnswerOptions({ options, selected, onSelect }: Props) {
  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
      {options.map((option, index) => {
        const isSelected = selected === option;

        return (
          <button
            key={option}
            onClick={() => onSelect(option)}
            className={`rounded-[22px] border-4 p-0 text-left transition ${
              isSelected
                ? 'border-[#1f2937] bg-[#ffde73] shadow-[6px_6px_0_#1f2937]'
                : 'border-[#1f2937] bg-[#fffaf0] shadow-[6px_6px_0_#1f2937] hover:translate-x-0.5 hover:translate-y-0.5 hover:shadow-[4px_4px_0_#1f2937]'
            }`}
          >
            <div className="border-b-4 border-[#1f2937] px-4 py-3">
              <div className="flex items-center justify-between">
                <span className="text-xs font-black uppercase tracking-[0.22em] text-[#6b7280]">
                  Option {index + 1}
                </span>
                {isSelected ? (
                  <BadgeCheck size={20} className="text-[#1f2937]" />
                ) : (
                  <Ticket size={20} className="text-[#1f2937]" />
                )}
              </div>
            </div>

            <div className="px-4 py-5">
              <div className="text-4xl font-black text-[#1f2937]">{option}</div>
              <div className="mt-2 text-sm font-medium text-[#4b5563]">
                {isSelected ? 'Selected answer' : 'Click to select'}
              </div>
            </div>
          </button>
        );
      })}
    </div>
  );
}