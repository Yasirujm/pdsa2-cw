'use client';

type BoardViewProps = {
  boardSize: number;
  jumps: Record<number, number>;
};

export default function BoardView({ boardSize, jumps }: BoardViewProps) {
  const totalCells = boardSize * boardSize;
  const cells = Array.from({ length: totalCells }, (_, i) => i + 1).reverse();

  return (
    <div
      className="grid gap-2"
      style={{ gridTemplateColumns: `repeat(${boardSize}, minmax(0, 1fr))` }}
    >
      {cells.map((cell) => {
        const jump = jumps[cell];
        const isLadder = jump && jump > cell;
        const isSnake = jump && jump < cell;

        return (
          <div
            key={cell}
            className="rounded-xl border border-slate-700 p-3 min-h-[72px] bg-slate-900 shadow-sm"
          >
            <div className="font-bold text-sm text-white">{cell}</div>

            {isLadder && (
              <div className="text-emerald-400 text-xs mt-1 font-medium">
                Ladder to {jump}
              </div>
            )}

            {isSnake && (
              <div className="text-red-400 text-xs mt-1 font-medium">
                Snake to {jump}
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
}