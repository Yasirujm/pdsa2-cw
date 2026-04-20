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
      className="grid gap-3"
      style={{ gridTemplateColumns: `repeat(${boardSize}, minmax(0, 1fr))` }}
    >
      {cells.map((cell, index) => {
        const jump = jumps[cell];
        const isLadder = jump && jump > cell;
        const isSnake = jump && jump < cell;
        const isAlt = index % 2 === 0;

        return (
          <div
            key={cell}
            className={`min-h-[104px] rounded-[20px] border-4 border-[#1f2937] p-3 shadow-[4px_4px_0_#1f2937] ${
              isLadder
                ? 'bg-[#fff3b0]'
                : isSnake
                ? 'bg-[#ffd6d6]'
                : isAlt
                ? 'bg-[#f8fafc]'
                : 'bg-[#eef6ff]'
            }`}
          >
            <div className="flex items-start justify-between gap-2">
              <div className="rounded-[12px] border-2 border-[#1f2937] bg-white px-2 py-1 text-sm font-black text-[#1f2937]">
                {cell}
              </div>

              {isLadder && (
                <div className="rounded-full border-2 border-[#1f2937] bg-white px-2 py-1 text-[11px] font-black">
                  🪜
                </div>
              )}

              {isSnake && (
                <div className="rounded-full border-2 border-[#1f2937] bg-white px-2 py-1 text-[11px] font-black">
                  🐍
                </div>
              )}
            </div>

            <div className="mt-4">
              {isLadder && (
                <>
                  <div className="text-2xl">🪜</div>
                  <p className="mt-1 text-xs font-bold leading-5 text-[#5b3a00]">
                    Ladder to <span className="text-[#1f2937]">{jump}</span>
                  </p>
                </>
              )}

              {isSnake && (
                <>
                  <div className="text-2xl">🐍</div>
                  <p className="mt-1 text-xs font-bold leading-5 text-[#7f1d1d]">
                    Snake to <span className="text-[#1f2937]">{jump}</span>
                  </p>
                </>
              )}

              {!isLadder && !isSnake && (
                <>
                  <div className="text-xl">·</div>
                  <p className="mt-1 text-xs font-semibold text-[#6b7280]">
                    Normal cell
                  </p>
                </>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}