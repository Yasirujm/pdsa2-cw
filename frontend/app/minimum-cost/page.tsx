'use client';

import { useState } from 'react';
import CostMatrixView from '../../components/minimum-cost/CostMatrixView';
import AnswerOptions from '../../components/minimum-cost/AnswerOptions';
import ResultBanner from '../../components/minimum-cost/ResultBanner';
import { MinCostcreateRound, MinCostsubmitAnswer } from '../../lib/api';

type RoundData = {
  gameRoundId: number;
  taskCount: number;
  costMatrix: number[][];
  answerChoices: number[];
  hungarianAnswer: number;
  minCostFlowAnswer: number;
  hungarianTimeNanos: number;
  minCostFlowTimeNanos: number;
};

export default function MinimumCostPage() {
  const [playerName, setPlayerName] = useState('');
  const [taskCount, setTaskCount] = useState(50);
  const [round, setRound] = useState<RoundData | null>(null);
  const [selectedAnswer, setSelectedAnswer] = useState<number | null>(null);
  const [result, setResult] = useState<'WIN' | 'LOSE' | 'DRAW' | null>(null);
  const [correctAnswer, setCorrectAnswer] = useState<number | undefined>(undefined);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleStart = async () => {
    if (taskCount < 50 || taskCount > 100) {
      setError('Task count must be between 50 and 100');
      return;
    }

    try {
      setLoading(true);
      setError('');
      setResult(null);
      setCorrectAnswer(undefined);
      setSelectedAnswer(null);

      const data = await MinCostcreateRound(taskCount);
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

      const response = await MinCostsubmitAnswer({
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
        <h1 className="text-3xl font-bold">Minimum Cost Game</h1>
        <p className="text-slate-600 mt-2">
          Find the minimum total assignment cost for tasks and employees.
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
          <label className="block text-sm font-medium mb-2">Task Count (50 - 100)</label>
          <input
            type="number"
            min={50}
            max={100}
            value={taskCount}
            onChange={(e) => setTaskCount(Number(e.target.value))}
            className="w-full border rounded-lg px-3 py-2"
          />
        </div>

        <div className="md:col-span-2 flex gap-3">
          <button
            onClick={handleStart}
            disabled={loading}
            className="rounded-xl bg-blue-600 text-white px-4 py-2 font-semibold disabled:opacity-50"
          >
            {loading ? 'Loading...' : 'Start Round'}
          </button>

          <button
            onClick={handleSubmit}
            disabled={loading || !round}
            className="rounded-xl bg-emerald-600 text-white px-4 py-2 font-semibold disabled:opacity-50"
          >
            Submit Answer
          </button>
        </div>
      </div>

      {error && <div className="text-red-600 font-medium">{error}</div>}

      {round && (
        <>
          <div className="bg-white rounded-2xl border p-4">
            <div className="font-semibold mb-2">Choose the minimum total cost</div>
            <AnswerOptions
              options={round.answerChoices}
              selected={selectedAnswer}
              onSelect={setSelectedAnswer}
            />
          </div>

          <ResultBanner result={result} correctAnswer={correctAnswer} />

          <div className="bg-white rounded-2xl border p-4 space-y-4">
            <div className="grid md:grid-cols-2 gap-4">
              <div className="rounded-xl bg-slate-50 p-3 border">
                <div className="font-semibold">Hungarian Algorithm</div>
                <div>Answer: ${round.hungarianAnswer}</div>
                <div>Time: {round.hungarianTimeNanos} ns</div>
              </div>

              <div className="rounded-xl bg-slate-50 p-3 border">
                <div className="font-semibold">Min-Cost Max-Flow</div>
                <div>Answer: ${round.minCostFlowAnswer}</div>
                <div>Time: {round.minCostFlowTimeNanos} ns</div>
              </div>
            </div>

            <CostMatrixView matrix={round.costMatrix} />
          </div>
        </>
      )}
    </div>
  );
}