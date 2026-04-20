import { useState } from 'react';
import { useRouter } from 'next/router';
import ChessBoard from '../components/ChessBoard';
import ResultModal from '../components/ResultModal';

const API_BASE = 'http://localhost:8080/api/queens';

export default function GamePage() {
  const router = useRouter();
  const { name } = router.query;
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [boardKey, setBoardKey] = useState(0);

  const handleSubmit = async (placement) => {
    if (!name) { alert('Player name missing.'); return; }
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
      fontFamily: "'Segoe UI', sans-serif",
      padding: '20px',
    }}>
      {/* Header */}
      <div style={{
        maxWidth: '900px',
        margin: '0 auto 24px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
      }}>
        <button onClick={() => router.push('/')} style={{
          background: 'rgba(255,255,255,0.1)',
          border: '1px solid rgba(255,255,255,0.15)',
          color: '#fff',
          padding: '8px 16px',
          borderRadius: '8px',
          cursor: 'pointer',
          fontSize: '14px',
        }}>← Back</button>

        <div style={{ textAlign: 'center' }}>
          <h1 style={{ color: '#fff', margin: 0, fontSize: '22px', fontWeight: '700' }}>♛ 16-Queens Puzzle</h1>
          <p style={{ color: 'rgba(255,255,255,0.5)', margin: 0, fontSize: '13px' }}>
            Playing as <span style={{ color: '#a5b4fc', fontWeight: '600' }}>{name}</span>
          </p>
        </div>

        <button onClick={() => router.push('/leaderboard')} style={{
          background: 'rgba(99,102,241,0.3)',
          border: '1px solid rgba(99,102,241,0.5)',
          color: '#a5b4fc',
          padding: '8px 16px',
          borderRadius: '8px',
          cursor: 'pointer',
          fontSize: '14px',
        }}>🏆 Leaderboard</button>
      </div>

      {/* Loading bar */}
      {loading && (
        <div style={{ textAlign: 'center', color: '#a5b4fc', marginBottom: '16px', fontSize: '15px' }}>
          ⏳ Checking your solution...
        </div>
      )}

      {/* Board card */}
      <div style={{
        maxWidth: '900px',
        margin: '0 auto',
        background: 'rgba(255,255,255,0.05)',
        backdropFilter: 'blur(20px)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '20px',
        padding: '30px',
        display: 'flex',
        justifyContent: 'center',
        overflowX: 'auto',
      }}>
        <ChessBoard key={boardKey} onSubmit={handleSubmit} />
      </div>

      {/* Instruction bar */}
      <div style={{
        maxWidth: '900px',
        margin: '16px auto 0',
        background: 'rgba(99,102,241,0.15)',
        border: '1px solid rgba(99,102,241,0.3)',
        borderRadius: '12px',
        padding: '12px 20px',
        color: 'rgba(255,255,255,0.7)',
        fontSize: '13px',
        textAlign: 'center',
      }}>
        💡 Click a cell to place a queen in that column. <strong style={{ color: '#ffd700' }}>Yellow</strong> = queen placed.
        <strong style={{ color: '#ff6b6b' }}> Red</strong> = conflict. Place all 16 with no conflicts, then Submit.
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