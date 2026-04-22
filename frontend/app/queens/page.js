'use client';

import { useState, Suspense } from 'react';
import { useRouter } from 'next/navigation';
import ChessBoard from '../../components/ChessBoard';
import ResultModal from '../../components/ResultModal';

const API_BASE = 'http://localhost:8080/api/queens';

function NameEntry({ onStart }) {
  const [name, setName] = useState('');
  const [error, setError] = useState('');

  const handleStart = () => {
    const trimmed = name.trim();
    if (!/^[a-zA-Z\s]+$/.test(trimmed)) {
      setError('Name must contain only letters');
      return;
    }
    if (!trimmed) { setError('Please enter your name.'); return; }
    if (trimmed.length < 2) { setError('Name must be at least 2 characters.'); return; }
    if (trimmed.length > 50) { setError('Name must be less than 50 characters.'); return; }
    onStart(trimmed);
  };

  return (
    <div style={{
      minHeight: '100vh',
      background: 'linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%)',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      fontFamily: "'Segoe UI', sans-serif", padding: '20px',
    }}>
      <div style={{
        background: 'rgba(255,255,255,0.05)',
        backdropFilter: 'blur(20px)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '24px', padding: '50px 40px',
        maxWidth: '440px', width: '100%', textAlign: 'center',
        boxShadow: '0 25px 50px rgba(0,0,0,0.5)',
      }}>
        <div style={{ fontSize: '72px', marginBottom: '8px' }}>♛</div>
        <h1 style={{ color: '#fff', fontSize: '28px', fontWeight: '700', margin: '0 0 8px' }}>
          8-Queens Puzzle
        </h1>
        <div style={{
          display: 'inline-block',
          background: 'rgba(99,102,241,0.3)',
          border: '1px solid rgba(99,102,241,0.5)',
          borderRadius: '20px', padding: '4px 16px',
          color: '#a5b4fc', fontSize: '13px', marginBottom: '16px',
        }}>
          16×16 Board
        </div>
        <p style={{
          color: 'rgba(255,255,255,0.6)', fontSize: '14px',
          lineHeight: '1.7', marginBottom: '28px',
        }}>
          Place 8 queens on a 16×16 chessboard so that no two queens threaten each other.
          Find a <strong style={{ color: '#a5b4fc' }}>unique solution</strong> nobody has claimed yet!
        </p>

        <input
          type="text"
          value={name}
          onChange={e => {
            const value = e.target.value;

            if (/^[a-zA-Z\s]*$/.test(value)) {
              setName(value);
              setError('');
            } else {
              setError('Name can only contain letters');
            }
          }}
          onKeyDown={e => e.key === 'Enter' && handleStart()}
          placeholder="Enter your name"
          style={{
            width: '100%', padding: '14px 18px', borderRadius: '12px',
            border: error ? '2px solid #f87171' : '2px solid rgba(255,255,255,0.15)',
            background: 'rgba(255,255,255,0.08)', color: '#fff',
            fontSize: '16px', outline: 'none', boxSizing: 'border-box',
            marginBottom: '8px',
          }}
        />
        {error && <p style={{ color: '#f87171', fontSize: '13px', marginBottom: '8px', textAlign: 'left' }}>{error}</p>}

        <button
          onClick={handleStart}
          style={{
            width: '100%', padding: '15px', borderRadius: '12px', border: 'none',
            background: 'linear-gradient(135deg, #6366f1, #8b5cf6)',
            color: '#fff', fontSize: '16px', fontWeight: '600',
            cursor: 'pointer', marginBottom: '10px',
            boxShadow: '0 4px 15px rgba(99,102,241,0.4)',
          }}
        >
          Start Playing →
        </button>

        <button
          onClick={() => window.location.href = '/queens/leaderboard'}
          style={{
            width: '100%', padding: '13px', borderRadius: '12px',
            border: '1px solid rgba(255,255,255,0.15)', background: 'transparent',
            color: 'rgba(255,255,255,0.7)', fontSize: '15px', cursor: 'pointer',
          }}
        >
          🏆 View Leaderboard
        </button>

        <div style={{ display: 'flex', justifyContent: 'center', gap: '24px', marginTop: '28px' }}>
          {[['172K+', 'Solutions'], ['16×16', 'Board'], ['8', 'Queens']].map(([val, label]) => (
            <div key={label} style={{ textAlign: 'center' }}>
              <div style={{ color: '#a5b4fc', fontSize: '18px', fontWeight: '700' }}>{val}</div>
              <div style={{ color: 'rgba(255,255,255,0.4)', fontSize: '11px' }}>{label}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

function GameBoard({ name, onBack }) {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [boardKey, setBoardKey] = useState(0);

  const handleSubmit = async (placement) => {
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE}/submit`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ playerName: name, placement }),
      });
      if (!response.ok) throw new Error();
      const data = await response.json();
      setResult(data);
      setShowModal(true);
    } catch {
      alert('Cannot connect to backend. Make sure Spring Boot is running on port 8080.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      minHeight: '100vh',
      background: 'linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%)',
      fontFamily: "'Segoe UI', sans-serif", padding: '20px',
    }}>
      <div style={{ maxWidth: '900px', margin: '0 auto' }}>

        {/* Header */}
        <div style={{
          display: 'flex', alignItems: 'center',
          justifyContent: 'space-between', marginBottom: '24px',
        }}>
          <button
            onClick={onBack}
            style={{
              background: 'rgba(255,255,255,0.1)',
              border: '1px solid rgba(255,255,255,0.15)',
              color: '#fff', padding: '8px 16px',
              borderRadius: '8px', cursor: 'pointer', fontSize: '14px',
            }}
          >
            ← Back
          </button>

          <div style={{ textAlign: 'center' }}>
            <h1 style={{ color: '#fff', margin: 0, fontSize: '22px', fontWeight: '700' }}>
              ♛ 8-Queens Puzzle (16×16 Board)
            </h1>
            <p style={{ color: 'rgba(255,255,255,0.5)', margin: 0, fontSize: '13px' }}>
              Playing as <span style={{ color: '#a5b4fc', fontWeight: '600' }}>{name}</span>
            </p>
          </div>

          <button
            onClick={() => window.location.href = '/queens/leaderboard'}
            style={{
              background: 'rgba(99,102,241,0.3)',
              border: '1px solid rgba(99,102,241,0.5)',
              color: '#a5b4fc', padding: '8px 16px',
              borderRadius: '8px', cursor: 'pointer', fontSize: '14px',
            }}
          >
            🏆 Leaderboard
          </button>
        </div>

        {loading && (
          <div style={{ textAlign: 'center', color: '#a5b4fc', marginBottom: '16px', fontSize: '15px' }}>
            ⏳ Checking your solution...
          </div>
        )}

        {/* Board */}
        <div style={{
          background: 'rgba(255,255,255,0.05)',
          border: '1px solid rgba(255,255,255,0.1)',
          borderRadius: '20px', padding: '30px',
          display: 'flex', justifyContent: 'center', overflowX: 'auto',
        }}>
          <ChessBoard key={boardKey} onSubmit={handleSubmit} />
        </div>

        {/* Instructions */}
        <div style={{
          marginTop: '16px',
          background: 'rgba(99,102,241,0.15)',
          border: '1px solid rgba(99,102,241,0.3)',
          borderRadius: '12px', padding: '12px 20px',
          color: 'rgba(255,255,255,0.7)', fontSize: '13px', textAlign: 'center',
        }}>
          💡 Click any cell to place a queen.
          <strong style={{ color: '#ffd700' }}> Yellow</strong> = placed.
          <strong style={{ color: '#ff6b6b' }}> Red</strong> = conflict.
          Place exactly <strong style={{ color: '#a5b4fc' }}>8 queens</strong> with
          no conflicts anywhere on the board, then Submit.
        </div>
      </div>

      {showModal && result && (
        <ResultModal
          result={result.result}
          message={result.message}
          onPlayAgain={() => { setShowModal(false); setBoardKey(p => p + 1); }}
        />
      )}
    </div>
  );
}

export default function QueensPage() {
  const [playerName, setPlayerName] = useState(null);

  if (!playerName) {
    return <NameEntry onStart={setPlayerName} />;
  }

  return <GameBoard name={playerName} onBack={() => setPlayerName(null)} />;
}