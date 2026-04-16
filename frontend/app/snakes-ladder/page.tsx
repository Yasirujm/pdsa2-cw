'use client';

import { useState } from 'react';
import BoardView from '../../components/BoardView';
import AnswerOptions from '../../components/AnswerOptions';
import ResultBanner from '../../components/ResultBanner';
import { createRound, submitAnswer } from '../../lib/api';

export default function SnakesLadderPage() {
  const [playerName, setPlayerName] = useState('');
  const [boardSize, setBoardSize] = useState(6);
  const [round, setRound] = useState<any>(null);
  const [selectedAnswer, setSelectedAnswer] = useState<number | null>(null);
  const [result, setResult] = useState<'WIN' | 'LOSE' | 'DRAW' | null>(null);
  const [correctAnswer, setCorrectAnswer] = useState<number | undefined>(undefined);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleStart = async () => {
    try {
      setLoading(true);
      setError('');
      setResult(null);
      setCorrectAnswer(undefined);
      setSelectedAnswer(null);

      const data = await createRound(boardSize);
      setRound(data);
    } catch (err) {
      setError('Failed to create game round');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async () => {
    if (!playerName.trim()) {
      setError('Please enter player name');
      return;
    }

    if (!round || selectedAnswer === null) {
      setError('Please start a round and select an answer');
      return;
    }

    try {
      setLoading(true);
      setError('');

      const response = await submitAnswer({
        playerName,
        gameRoundId: round.gameRoundId,
        selectedAnswer,
      });

      setResult(response.result);
      setCorrectAnswer(response.correctAnswer);
    } catch (err) {
      setError('Failed to submit answer');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-6xl mx-auto p-6 space-y-6">
      <div>
        <h1 className="text-3xl font-bold">Snake and Ladder Game Problem</h1>
        <p className="text-slate-600 mt-2">
          Find the minimum number of dice throws required to reach the last cell.
        </p>
      </div>

      <div className="grid md:grid-cols-2 gap-4 bg-slate-50 rounded-2xl p-4 border">
        <div>
          <label className="block text-sm font-medium mb-2">Player Name</label>
          <input
            value={playerName}
            onChange={(e) => setPlayerName(e.target.value)}
            className="w-full border rounded-lg px-3 py-2"
            placeholder="Enter your name"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">Board Size (6 - 12)</label>
          <input
            type="number"
            min={6}
            max={12}
            value={boardSize}
            onChange={(e) => setBoardSize(Number(e.target.value))}
            className="w-full border rounded-lg px-3 py-2"
          />
        </div>

        <div className="md:col-span-2 flex gap-3">
          <button
            onClick={handleStart}
            disabled={loading}
            className="rounded-xl bg-blue-600 text-white px-4 py-2 font-semibold"
          >
            {loading ? 'Loading...' : 'Start Round'}
          </button>

          <button
            onClick={handleSubmit}
            disabled={loading || !round}
            className="rounded-xl bg-emerald-600 text-white px-4 py-2 font-semibold"
          >
            Submit Answer
          </button>
        </div>
      </div>

      {error && <div className="text-red-600 font-medium">{error}</div>}

      {round && (
        <>
          <div className="bg-white rounded-2xl border p-4">
            <div className="font-semibold mb-2">
              Choose the minimum number of dice throws
            </div>
            <AnswerOptions
              options={round.answerChoices}
              selected={selectedAnswer}
              onSelect={setSelectedAnswer}
            />
          </div>

          <ResultBanner result={result} correctAnswer={correctAnswer} />

          <div className="bg-white rounded-2xl border p-4">
            <div className="grid md:grid-cols-2 gap-4 mb-4">
              <div className="rounded-xl bg-slate-50 p-3 border">
                <div className="font-semibold">BFS</div>
                <div>Answer: {round.bfsAnswer}</div>
                <div>Time: {round.bfsTimeNanos} ns</div>
              </div>

              <div className="rounded-xl bg-slate-50 p-3 border">
                <div className="font-semibold">Dijkstra</div>
                <div>Answer: {round.dijkstraAnswer}</div>
                <div>Time: {round.dijkstraTimeNanos} ns</div>
              </div>
            </div>

            <BoardView boardSize={round.boardSize} jumps={round.jumps} />
          </div>
        </>
      )}
    </div>
  );
}