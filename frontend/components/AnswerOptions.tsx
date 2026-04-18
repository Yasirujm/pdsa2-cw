'use client';
type Props = {
    options: number[];
    selected: number | null;
    onSelect: (value: number) => void;
};

export default function AnswerOptions({ options, selected, onSelect }: Props) {
    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mt-4">
            {options.map((option) => (
                <button key={option} onClick={() => onSelect(option)} className={`rounded-xl border px-4 py-3 font-semibold transition ${ selected === option ? 'bg-blue-600 text-white' : 'bg-white hover:bgslate-50'}`}>
                    {option}
                </button>
            ))}
        </div>
    );
}
