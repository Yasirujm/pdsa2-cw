'use client';

type Props = {
  result: 'WIN' | 'LOSE' | 'DRAW' | null;
  correctAnswer?: number;
};

export default function ResultBanner({ result, correctAnswer }: Props) {
  if (!result) return null;

  const config =
    result === 'WIN'
      ? {
          emoji: '✅',
          title: 'Correct Answer',
          subtitle: 'Nice work. Your selected cost matches the optimal result.',
          classes: 'border-emerald-500/30 bg-emerald-500/10 text-emerald-300',
        }
      : result === 'LOSE'
      ? {
          emoji: '❌',
          title: 'Incorrect Answer',
          subtitle: 'Your guess was not the minimum total cost for this round.',
          classes: 'border-rose-500/30 bg-rose-500/10 text-rose-300',
        }
      : {
          emoji: '⚖️',
          title: 'Draw',
          subtitle: 'Both outcomes are equivalent for this round.',
          classes: 'border-amber-500/30 bg-amber-500/10 text-amber-300',
        };

  return (
    <div className={`rounded-[24px] border p-5 md:p-6 ${config.classes}`}>
      <div className="flex items-start gap-4">
        <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-black/20 text-2xl">
          {config.emoji}
        </div>

        <div className="flex-1">
          <div className="text-xl font-bold">{config.title}</div>
          <p className="mt-1 text-sm leading-6 text-slate-200/90">{config.subtitle}</p>

          {result === 'LOSE' && correctAnswer !== undefined && (
            <div className="mt-4 inline-flex items-center gap-2 rounded-full border border-white/10 bg-black/20 px-4 py-2 text-sm text-slate-100">
              Correct cost:
              <span className="font-bold text-white">{correctAnswer}</span>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}