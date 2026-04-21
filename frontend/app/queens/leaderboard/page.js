'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

export default function LeaderboardPage() {
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const router = useRouter();

  useEffect(() => {
    fetch('http://localhost:8080/api/queens/leaderboard')
      .then(r => { if (!r.ok) throw new Error(); return r.json(); })
      .then(d => { setSessions(d); setLoading(false); })
      .catch(() => { setError(true); setLoading(false); });
  }, []);

  const medals = ['🥇', '🥈', '🥉'];

  return (
    <div style={{
      minHeight: '100vh',
      background: 'linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%)',
      fontFamily: "'Segoe UI', sans-serif",
      padding: '40px 20px',
    }}>
      <div style={{ maxWidth: '680px', margin: '0 auto' }}>

        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '24px' }}>
          <button
            onClick={() => router.push('/queens')}
            style={{
              background: 'rgba(255,255,255,0.1)',
              border: '1px solid rgba(255,255,255,0.15)',
              color: '#fff', padding: '8px 16px',
              borderRadius: '8px', cursor: 'pointer', fontSize: '14px',
            }}
          >
            ← Back to Game
          </button>
          <h1 style={{ color: '#fff', fontSize: '24px', fontWeight: '700', margin: 0 }}>
            🏆 Leaderboard
          </h1>
          <div style={{ width: '100px' }} />
        </div>

        <div style={{
          background: 'rgba(255,255,255,0.05)',
          border: '1px solid rgba(255,255,255,0.1)',
          borderRadius: '20px', overflow: 'hidden',
        }}>
          {loading && (
            <div style={{ padding: '60px', textAlign: 'center', color: 'rgba(255,255,255,0.5)' }}>
              Loading...
            </div>
          )}
          {error && (
            <div style={{ padding: '60px', textAlign: 'center', color: '#f87171' }}>
              Cannot connect to backend. Make sure Spring Boot is running on port 8080.
            </div>
          )}
          {!loading && !error && sessions.length === 0 && (
            <div style={{ padding: '60px', textAlign: 'center', color: 'rgba(255,255,255,0.5)' }}>
              No winners yet. Be the first to find a valid solution!
            </div>
          )}
          {!loading && !error && sessions.length > 0 && (
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid rgba(255,255,255,0.1)' }}>
                  {['Rank', 'Player', 'Date'].map(h => (
                    <th key={h} style={{
                      padding: '16px 20px', textAlign: 'left',
                      color: 'rgba(255,255,255,0.4)', fontSize: '12px',
                      fontWeight: '600', textTransform: 'uppercase', letterSpacing: '1px',
                    }}>{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {sessions.map((s, i) => (
                  <tr key={s.id} style={{
                    borderBottom: '1px solid rgba(255,255,255,0.05)',
                    background: i < 3 ? 'rgba(99,102,241,0.08)' : 'transparent',
                  }}>
                    <td style={{ padding: '16px 20px', fontSize: '20px' }}>
                      {medals[i] || <span style={{ color: 'rgba(255,255,255,0.3)', fontSize: '14px' }}>#{i + 1}</span>}
                    </td>
                    <td style={{ padding: '16px 20px', color: '#fff', fontWeight: '600' }}>
                      {s.playerName}
                    </td>
                    <td style={{ padding: '16px 20px', color: 'rgba(255,255,255,0.4)', fontSize: '13px' }}>
                      {new Date(s.playedAt).toLocaleString()}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
}