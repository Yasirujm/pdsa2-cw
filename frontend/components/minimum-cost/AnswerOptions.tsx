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
    <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mt-4">
      {options.map((option) => (
        <button
          key={option}
          onClick={() => onSelect(option)}
          disabled={disabled}
          className={`rounded-xl border px-4 py-3 font-semibold transition ${
            selected === option
              ? 'bg-blue-600 text-white border-blue-500'
              : 'bg-slate-900 text-slate-200 border-slate-700 hover:bg-slate-800'
          } disabled:opacity-50 disabled:cursor-not-allowed`}
        >
          {option}
        </button>
      ))}
    </div>
  );
}