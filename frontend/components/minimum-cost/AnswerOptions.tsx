'use client';

type Props = {
  options: number[];
  selected: number | null;
  onSelect: (value: number) => void;
  disabled?: boolean;
};

export default function AnswerOptions({
  options,
  selected,
  onSelect,
  disabled = false,
}: Props) {
  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
      {options.map((option, index) => {
        const isSelected = selected === option;

        return (
          <button
            key={`${option}-${index}`}
            onClick={() => onSelect(option)}
            disabled={disabled}
            className={`group relative overflow-hidden rounded-[24px] border p-5 text-left transition-all duration-200 ${
              isSelected
                ? 'border-amber-400 bg-amber-500/15 shadow-[0_0_0_1px_rgba(251,191,36,0.25)]'
                : 'border-white/10 bg-slate-900/60 hover:-translate-y-0.5 hover:border-rose-300/30 hover:bg-slate-800/70'
            } disabled:cursor-not-allowed disabled:opacity-50`}
          >
            <div className="absolute right-3 top-3 rounded-full border border-white/10 bg-white/5 px-2 py-1 text-[10px] font-bold uppercase tracking-wider text-slate-400">
              Choice {index + 1}
            </div>

            <div className="mt-6 flex items-start justify-between gap-3">
              <div>
                <div className="text-xs uppercase tracking-[0.2em] text-slate-400">
                  Total Cost
                </div>
                <div
                  className={`mt-2 text-3xl font-bold ${
                    isSelected ? 'text-amber-300' : 'text-white'
                  }`}
                >
                  {option}
                </div>
              </div>

              <div
                className={`flex h-10 w-10 items-center justify-center rounded-2xl border text-sm font-bold ${
                  isSelected
                    ? 'border-amber-300 bg-amber-400/20 text-amber-200'
                    : 'border-white/10 bg-white/5 text-slate-300'
                }`}
              >
                {index + 1}
              </div>
            </div>

            <div className="mt-4 text-sm text-slate-400">
              {isSelected ? 'Selected answer' : 'Click to choose this value'}
            </div>
          </button>
        );
      })}
    </div>
  );
}