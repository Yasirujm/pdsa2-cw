export default function ResultModal({ result, message, onPlayAgain }) {
  const config = {
    WIN:  { gradient: 'linear-gradient(135deg, #059669, #10b981)', icon: '🏆', title: 'You Win!', glow: 'rgba(16,185,129,0.3)' },
    LOSE: { gradient: 'linear-gradient(135deg, #dc2626, #ef4444)', icon: '❌', title: 'Wrong Placement!', glow: 'rgba(239,68,68,0.3)' },
    DRAW: { gradient: 'linear-gradient(135deg, #d97706, #f59e0b)', icon: '🤝', title: 'Already Claimed!', glow: 'rgba(245,158,11,0.3)' },
  }[result];

  return (
    <div style={{
      position: 'fixed', inset: 0,
      background: 'rgba(0,0,0,0.75)',
      backdropFilter: 'blur(8px)',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      zIndex: 50, padding: '20px',
    }}>
      <div style={{
        background: '#1a1a2e',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '24px',
        padding: '48px 40px',
        maxWidth: '420px', width: '100%',
        textAlign: 'center',
        boxShadow: `0 0 60px ${config.glow}`,
        fontFamily: "'Segoe UI', sans-serif",
      }}>
        <div style={{ fontSize: '72px', marginBottom: '16px' }}>{config.icon}</div>
        <h2 style={{ color: '#fff', fontSize: '26px', fontWeight: '700', margin: '0 0 12px' }}>
          {config.title}
        </h2>
        <p style={{ color: 'rgba(255,255,255,0.65)', fontSize: '15px', lineHeight: '1.7', margin: '0 0 32px' }}>
          {message}
        </p>
        <button
          onClick={onPlayAgain}
          style={{
            width: '100%', padding: '15px',
            borderRadius: '12px', border: 'none',
            background: config.gradient,
            color: '#fff', fontSize: '16px',
            fontWeight: '600', cursor: 'pointer',
            boxShadow: `0 4px 20px ${config.glow}`,
          }}
        >
          Play Again
        </button>
      </div>
    </div>
  );
}