const API_URL = 'http://localhost:8080/api/snakes-ladder';

export async function createRound(boardSize: number) {
    const res = await fetch(`${API_URL}/round`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ boardSize }),
    });

    if (!res.ok) throw new Error('Failed to create round');
    return res.json();
}

export async function submitAnswer(payload: {playerName: string; gameRoundId: number; selectedAnswer: number;}) {
    const res = await fetch(`${API_URL}/answer`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
    });

    if (!res.ok) throw new Error('Failed to submit answer');
    return res.json();
}
