import { useState } from 'react';
import { useRouter } from 'next/router';

export default function HomePage() {
  const [name, setName] = useState('');
  const [error, setError] = useState('');
  const router = useRouter();

  const handleStart = () => {
    const trimmed = name.trim();
    if (!trimmed) { setError('Please enter your name.'); return; }
    if (trimmed.length < 2) { setError('Name must be at least 2 characters.'); return; }
    if (trimmed.length > 50) { setError('Name must be less than 50 characters.'); return; }
    router.push(`/game?name=${encodeURIComponent(trimmed)}`);
  };

  return (
    <div style={{
      minHeight: '100vh',
      background: 'linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      fontFamily: "'Segoe UI', sans-serif",
      padding: '20px',
    }}>
      <div style={{
        background: 'rgba(255,255,255,0.05)',
        backdropFilter: 'blur(20px)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '24px',
        padding: '50px 40px',
        maxWidth: '440px',
        width: '100%',
        textAlign: 'center',
        boxShadow: '0 25px 50px rgba(0,0,0,0.5)',
      }}>
        <div style={{ fontSize: '72px', marginBottom: '8px' }}>♛</div>
        <h1 style={{ color: '#fff', fontSize: '32px', fontWeight: '700', margin: '0 0 8px' }}>
          16-Queens Puzzle
        </h1>
        <div style={{
          display: 'inline-block',
          background: 'rgba(99,102,241,0.3)',
          border: '1px solid rgba(99,102,241,0.5)',
          borderRadius: '20px',
          padding: '4px 16px',
          color: '#a5b4fc',
          fontSize: '13px',
          marginBottom: '20px',
        }}>
          PDSA Coursework — Task 5
        </div>
        <p style={{ color: 'rgba(255,255,255,0.6)', fontSize: '14px', lineHeight: '1.7', marginBottom: '32px' }}>
          Place 16 queens on a 16×16 chessboard so that no two queens threaten each other.
          Find a <strong style={{ color: '#a5b4fc' }}>unique solution</strong> nobody has claimed yet!
        </p>

        <div style={{ marginBottom: '8px' }}>
          <input
            type="text"
            value={name}
            onChange={e => { setName(e.target.value); setError(''); }}
            onKeyDown={e => e.key === 'Enter' && handleStart()}
            placeholder="Enter your name"
            style={{
              width: '100%',
              padding: '14px 18px',
              borderRadius: '12px',
              border: error ? '2px solid #f87171' : '2px solid rgba(255,255,255,0.15)',
              background: 'rgba(255,255,255,0.08)',
              color: '#fff',
              fontSize: '16px',
              outline: 'none',
              boxSizing: 'border-box',
            }}
          />
        </div>
        {error && <p style={{ color: '#f87171', fontSize: '13px', marginBottom: '12px', textAlign: 'left' }}>{error}</p>}

        <button
          onClick={handleStart}
          style={{
            width: '100%',
            padding: '15px',
            borderRadius: '12px',
            border: 'none',
            background: 'linear-gradient(135deg, #6366f1, #8b5cf6)',
            color: '#fff',
            fontSize: '16px',
            fontWeight: '600',
            cursor: 'pointer',
            marginBottom: '12px',
            boxShadow: '0 4px 15px rgba(99,102,241,0.4)',
          }}
        >
          Start Playing →
        </button>

        <button
          onClick={() => router.push('/leaderboard')}
          style={{
            width: '100%',
            padding: '13px',
            borderRadius: '12px',
            border: '1px solid rgba(255,255,255,0.15)',
            background: 'transparent',
            color: 'rgba(255,255,255,0.7)',
            fontSize: '15px',
            cursor: 'pointer',
          }}
        >
          🏆 View Leaderboard
        </button>

        <div style={{ display: 'flex', justifyContent: 'center', gap: '24px', marginTop: '32px' }}>
          {[['14.7M', 'Solutions'], ['16×16', 'Board'], ['2', 'Algorithms']].map(([val, label]) => (
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