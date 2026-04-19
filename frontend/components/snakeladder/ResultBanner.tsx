'use client';

type Props = {
    result: 'WIN' | 'LOSE' | 'DRAW' | null;
    correctAnswer?: number;
};

export default function ResultBanner({ result }: Props) {
    if (!result) return null;

    const styles =
        result === 'WIN'
            ? 'bg-green-100 text-green-700 border-green-300'
            : result === 'LOSE'
            ? 'bg-red-100 text-red-700 border-red-300'
            : 'bg-yellow-100 text-yellow-700 border-yellow-300';

    return (
        <div className={`mt-4 rounded-xl border p-4 ${styles}`}>
            <div className="font-bold">{result}</div>
        </div>
    );
}