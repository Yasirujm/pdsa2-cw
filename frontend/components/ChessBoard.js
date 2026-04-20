import { useState } from 'react';

const BOARD = 16;
const NUM_QUEENS = 8;

export default function ChessBoard({ onSubmit }) {
  const [placed, setPlaced] = useState([]);

  const isQueenAt = (row, col) =>
    placed.some(q => q.row === row && q.col === col);

  const hasConflict = (row, col) => {
    return placed.some(q => {
      if (q.row === row && q.col === col) return false;
      if (q.row === row) return true;
      if (q.col === col) return true;
      if (Math.abs(q.row - row) === Math.abs(q.col - col)) return true;
      return false;
    });
  };

  const handleClick = (row, col) => {
    if (isQueenAt(row, col)) {
      setPlaced(prev => prev.filter(q =>
        !(q.row === row && q.col === col)
      ));
    } else if (placed.length < NUM_QUEENS) {
      setPlaced(prev => [...prev, { row, col }]);
    }
  };

  const handleSubmit = () => {
    if (placed.length !== NUM_QUEENS) {
      alert(`Place all 8 queens first. You have ${placed.length}.`);
      return;
    }
    const encoded = placed
      .map(q => q.col * BOARD + q.row)
      .sort((a, b) => a - b);
    onSubmit(encoded);
  };

  const allPlaced = placed.length === NUM_QUEENS;
  const hasAnyConflict = placed.some(q => hasConflict(q.row, q.col));

  return (
    <div style={{
      display: 'flex', flexDirection: 'column',
      alignItems: 'center', gap: '20px'
    }}>

      {/* Status bar */}
      <div style={{
        display: 'flex', alignItems: 'center', gap: '16px',
        background: 'rgba(255,255,255,0.05)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '12px', padding: '10px 20px',
      }}>
        <span style={{ color: 'rgba(255,255,255,0.6)', fontSize: '14px' }}>
          Queens placed:
        </span>
        <span style={{ color: '#a5b4fc', fontWeight: '700', fontSize: '18px' }}>
          {placed.length}
        </span>
        <span style={{ color: 'rgba(255,255,255,0.3)', fontSize: '14px' }}>
          / {NUM_QUEENS}
        </span>
        {allPlaced && !hasAnyConflict && (
          <span style={{ color: '#10b981', fontSize: '13px', fontWeight: '600' }}>
            ✓ Ready to submit!
          </span>
        )}
        {hasAnyConflict && (
          <span style={{ color: '#f87171', fontSize: '13px' }}>
            ⚠ Conflicts detected
          </span>
        )}
      </div>

      {/* Board */}
      <div style={{
        border: '3px solid rgba(255,255,255,0.2)',
        borderRadius: '4px',
        boxShadow: '0 20px 60px rgba(0,0,0,0.5)',
        display: 'grid',
        gridTemplateColumns: `repeat(${BOARD}, 34px)`,
        gridTemplateRows: `repeat(${BOARD}, 34px)`,
      }}>
        {Array.from({ length: BOARD }, (_, row) =>
          Array.from({ length: BOARD }, (_, col) => {
            const hasQueen = isQueenAt(row, col);
            const conflict = hasQueen && hasConflict(row, col);
            const light = (row + col) % 2 === 0;
            return (
              <div
                key={`${row}-${col}`}
                onClick={() => handleClick(row, col)}
                title={`Row ${row + 1}, Col ${col + 1}`}
                style={{
                  width: 34, height: 34,
                  backgroundColor: conflict
                    ? '#dc2626'
                    : hasQueen
                    ? '#ffd700'
                    : light
                    ? '#f0d9b5'
                    : '#b58863',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  cursor: placed.length < NUM_QUEENS || hasQueen
                    ? 'pointer' : 'not-allowed',
                  fontSize: 18,
                  userSelect: 'none',
                  transition: 'background-color 0.1s',
                }}
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
          onClick={() => setPlaced([])}
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
          disabled={!allPlaced}
          style={{
            padding: '12px 28px', borderRadius: '10px',
            border: 'none',
            background: allPlaced
              ? 'linear-gradient(135deg, #6366f1, #8b5cf6)'
              : 'rgba(255,255,255,0.1)',
            color: allPlaced ? '#fff' : 'rgba(255,255,255,0.3)',
            fontSize: '14px', fontWeight: '600',
            cursor: allPlaced ? 'pointer' : 'not-allowed',
            boxShadow: allPlaced
              ? '0 4px 15px rgba(99,102,241,0.4)' : 'none',
          }}
        >
          Submit Answer ✓
        </button>
      </div>

      <p style={{
        color: 'rgba(255,255,255,0.35)',
        fontSize: '12px', textAlign: 'center', maxWidth: '500px',
      }}>
        Click any cell to place a queen. Click again to remove.
        Place exactly 8 queens anywhere on the 16×16 board with no conflicts.
      </p>
    </div>
  );
}