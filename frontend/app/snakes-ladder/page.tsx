'use client';

import { useState } from 'react';
import BoardView from '../../components/snakeladder/BoardView';
import AnswerOptions from '../../components/snakeladder/AnswerOptions';
import ResultBanner from '../../components/snakeladder/ResultBanner';
import { createRound, submitAnswer } from '../../lib/api';

type RoundData = {
  gameRoundId: number;
  boardSize: number;
  totalCells: number;
  jumps: Record<number, number>;
  answerChoices: number[];
};

export default function SnakesLadderPage() {
  const [playerName, setPlayerName] = useState('');
  const [boardSize, setBoardSize] = useState(6);
  const [round, setRound] = useState<RoundData | null>(null);
  const [selectedAnswer, setSelectedAnswer] = useState<number | null>(null);
  const [result, setResult] = useState<'WIN' | 'LOSE' | 'DRAW' | null>(null);
  const [correctAnswer, setCorrectAnswer] = useState<number | undefined>(undefined);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const startRound = async () => {
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

  const handleRetry = async () => {
    try {
      setLoading(true);
      setError('');
      setResult(null);
      setCorrectAnswer(undefined);
      setSelectedAnswer(null);

      const data = await createRound(boardSize);
      setRound(data);
    } catch (err) {
      setError('Failed to create new round');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-200 flex flex-col items-center py-12 px-4">
      <div className="max-w-6xl w-full bg-slate-900 border border-slate-800 p-8 rounded-3xl shadow-2xl space-y-6">
        <div className="text-center">
          <h1 className="text-3xl font-bold text-white">Snake and Ladder Game</h1>
          <p className="text-slate-400 mt-2">
            Choose the minimum number of dice throws required to reach the last cell.
          </p>
        </div>

        <div className="grid md:grid-cols-2 gap-4 bg-slate-950 rounded-2xl p-4 border border-slate-800">
          <div>
            <label className="block text-sm font-medium mb-2 text-slate-300">Player Name</label>
            <input
              value={playerName}
              onChange={(e) => setPlayerName(e.target.value)}
              className="w-full border border-slate-700 bg-slate-900 text-white rounded-xl px-3 py-3 outline-none"
              placeholder="Enter your name"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-2 text-slate-300">Board Size (6 - 12)</label>
            <input
              type="number"
              min={6}
              max={12}
              value={boardSize}
              onChange={(e) => setBoardSize(Number(e.target.value))}
              className="w-full border border-slate-700 bg-slate-900 text-white rounded-xl px-3 py-3 outline-none"
            />
          </div>

          <div className="md:col-span-2 flex gap-3">
            <button
              onClick={startRound}
              disabled={loading}
              className="rounded-xl bg-blue-600 hover:bg-blue-500 text-white px-5 py-3 font-semibold disabled:opacity-50"
            >
              {loading ? 'Loading...' : 'Start Round'}
            </button>

            <button
              onClick={handleSubmit}
              disabled={loading || !round}
              className="rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white px-5 py-3 font-semibold disabled:opacity-50"
            >
              Submit Answer
            </button>
          </div>
        </div>

        {error && <div className="text-red-400 font-medium">{error}</div>}

        {round && (
          <>
            <div className="bg-slate-950 border border-slate-800 rounded-2xl p-4">
              <div className="font-semibold mb-2 text-white">
                Choose the minimum number of dice throws
              </div>
              <AnswerOptions
                options={round.answerChoices}
                selected={selectedAnswer}
                onSelect={setSelectedAnswer}
              />
            </div>

            <ResultBanner result={result} correctAnswer={correctAnswer} />

            {result && (
              <div className="flex justify-center">
                <button
                  onClick={handleRetry}
                  disabled={loading}
                  className="rounded-xl bg-slate-700 hover:bg-slate-600 text-white px-5 py-3 font-semibold disabled:opacity-50"
                >
                  {loading ? 'Loading...' : 'Retry'}
                </button>
              </div>
            )}

            <div className="bg-slate-950 border border-slate-800 rounded-2xl p-4">
              <BoardView boardSize={round.boardSize} jumps={round.jumps} />
            </div>
          </>
        )}
      </div>
    </div>
  );
}