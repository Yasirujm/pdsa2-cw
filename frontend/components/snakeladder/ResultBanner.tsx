'use client';

type Props = {
  result: 'WIN' | 'LOSE' | 'DRAW' | null;
  correctAnswer?: number;
};

export default function ResultBanner({ result, correctAnswer }: Props) {
  if (!result) return null;

  const styles =
    result === 'WIN'
      ? 'bg-emerald-900/30 text-emerald-400 border-emerald-500/50'
      : result === 'LOSE'
      ? 'bg-red-900/30 text-red-400 border-red-500/50'
      : 'bg-yellow-900/30 text-yellow-400 border-yellow-500/50';

  return (
    <div className={`mt-4 rounded-xl border p-4 text-center ${styles}`}>
      <div className="font-bold text-lg">{result === 'WIN' ? 'You Win!' : result === 'LOSE' ? 'Wrong Answer' : result}</div>

      {result === 'WIN' && (
        <div className="text-slate-300 mt-1">
          Correct answer selected.
        </div>
      )}

      {result === 'LOSE' && correctAnswer !== undefined && (
        <div className="text-slate-300 mt-1">
          Correct answer: <span className="font-bold text-emerald-400">{correctAnswer}</span>
        </div>
      )}
    </div>
  );
}