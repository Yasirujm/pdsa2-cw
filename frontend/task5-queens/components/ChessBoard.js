import { useState } from 'react';

const N = 16;

export default function ChessBoard({ onSubmit }) {
  const [queens, setQueens] = useState(Array(N).fill(-1));
  const [count, setCount] = useState(0);

  const handleClick = (col, row) => {
    const q = [...queens];
    if (q[col] === row) { q[col] = -1; setCount(c => c - 1); }
    else if (q[col] === -1) { q[col] = row; setCount(c => c + 1); }
    else { q[col] = row; }
    setQueens(q);
  };

  const conflict = (col, row) => {
    for (let c = 0; c < N; c++) {
      if (c === col || queens[c] === -1) continue;
      if (queens[c] === row || Math.abs(queens[c] - row) === Math.abs(c - col)) return true;
    }
    return false;
  };

  const handleSubmit = () => {
    if (count !== N) { alert(`Place all 16 queens first. Currently: ${count}`); return; }
    onSubmit(queens);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '20px' }}>
      {/* Status bar */}
      <div style={{
        display: 'flex', alignItems: 'center', gap: '16px',
        background: 'rgba(255,255,255,0.05)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '12px', padding: '10px 20px',
      }}>
        <span style={{ color: 'rgba(255,255,255,0.6)', fontSize: '14px' }}>Queens placed:</span>
        <span style={{ color: '#a5b4fc', fontWeight: '700', fontSize: '18px' }}>{count}</span>
        <span style={{ color: 'rgba(255,255,255,0.3)', fontSize: '14px' }}>/ 16</span>
        {count === 16 && <span style={{ color: '#10b981', fontSize: '13px', fontWeight: '600' }}>✓ Ready!</span>}
      </div>

      {/* Board */}
      <div style={{
        border: '3px solid rgba(255,255,255,0.2)',
        borderRadius: '4px',
        boxShadow: '0 20px 60px rgba(0,0,0,0.5)',
        display: 'grid',
        gridTemplateColumns: `repeat(${N}, 34px)`,
        gridTemplateRows: `repeat(${N}, 34px)`,
      }}>
        {Array.from({ length: N }, (_, row) =>
          Array.from({ length: N }, (_, col) => {
            const hasQueen = queens[col] === row;
            const isConflict = hasQueen && conflict(col, row);
            const light = (row + col) % 2 === 0;
            return (
              <div
                key={`${row}-${col}`}
                onClick={() => handleClick(col, row)}
                style={{
                  width: 34, height: 34,
                  backgroundColor: isConflict ? '#dc2626' : hasQueen ? '#ffd700' : light ? '#f0d9b5' : '#b58863',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  cursor: 'pointer', fontSize: 18, userSelect: 'none',
                  transition: 'background-color 0.1s',
                }}
                title={`Column ${col + 1}, Row ${row + 1}`}
              >
                {hasQueen ? '♛' : ''}
              </div>
            );
          })
        )}
      </div>

      {/* Buttons */}
      <div style={{ display: 'flex', gap: '12px' }}>
        <button
          onClick={() => { setQueens(Array(N).fill(-1)); setCount(0); }}
          style={{
            padding: '12px 28px', borderRadius: '10px',
            border: '1px solid rgba(255,255,255,0.2)',
            background: 'rgba(255,255,255,0.08)',
            color: '#fff', fontSize: '14px',
            fontWeight: '600', cursor: 'pointer',
          }}
        >
          Reset Board
        </button>
        <button
          onClick={handleSubmit}
          disabled={count !== N}
          style={{
            padding: '12px 28px', borderRadius: '10px', border: 'none',
            background: count === N
              ? 'linear-gradient(135deg, #6366f1, #8b5cf6)'
              : 'rgba(255,255,255,0.1)',
            color: count === N ? '#fff' : 'rgba(255,255,255,0.3)',
            fontSize: '14px', fontWeight: '600',
            cursor: count === N ? 'pointer' : 'not-allowed',
            boxShadow: count === N ? '0 4px 15px rgba(99,102,241,0.4)' : 'none',
          }}
        >
          Submit Answer ✓
        </button>
      </div>
    </div>
  );
}