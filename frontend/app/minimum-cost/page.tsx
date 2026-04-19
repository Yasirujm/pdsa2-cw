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
  const [taskCount, setTaskCount] = useState(4);
  const [useRandomSize, setUseRandomSize] = useState(false);
  const [round, setRound] = useState<RoundData | null>(null);
  const [selectedAnswer, setSelectedAnswer] = useState<number | null>(null);
  const [result, setResult] = useState<'WIN' | 'LOSE' | 'DRAW' | null>(null);
  const [correctAnswer, setCorrectAnswer] = useState<number | undefined>(undefined);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const hasSubmitted = result !== null;

  const handleStart = async () => {
    if (!useRandomSize && (taskCount < 2 || taskCount > 10)) {
      setError('For manual mode, task count must be between 2 and 10.');
      return;
    }

    try {
      setLoading(true);
      setError('');
      setResult(null);
      setCorrectAnswer(undefined);
      setSelectedAnswer(null);

      const data = await MinCostcreateRound({
        taskCount,
        useRandomSize,
      });

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

  const handleRetry = async () => {
    if (!useRandomSize && (taskCount < 2 || taskCount > 10)) {
      setError('For manual mode, task count must be between 2 and 10.');
      return;
    }

    try {
      setLoading(true);
      setError('');
      setResult(null);
      setCorrectAnswer(undefined);
      setSelectedAnswer(null);

      const data = await MinCostcreateRound({
        taskCount,
        useRandomSize,
      });

      setRound(data);
    } catch (err) {
      setError('Failed to create new round');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-200 py-12 px-4">
      <div className="max-w-6xl mx-auto space-y-6">
        <div className="bg-slate-900 border border-slate-800 rounded-3xl p-8 shadow-2xl space-y-6">
          <div>
            <h1 className="text-3xl font-bold text-white">Minimum Cost Game</h1>
            <p className="text-slate-400 mt-2">
              Find the minimum total assignment cost for tasks and employees.
            </p>
          </div>

          <div className="grid md:grid-cols-2 gap-4 bg-slate-900 rounded-2xl p-4 border border-slate-800">
            <div>
              <label className="block text-sm font-medium mb-2 text-slate-300">
                Player Name
              </label>
              <input
                value={playerName}
                onChange={(e) => setPlayerName(e.target.value)}
                className="w-full border border-slate-700 bg-slate-950 text-white rounded-lg px-3 py-2"
                placeholder="Enter your name"
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-2 text-slate-300">
                Mode
              </label>
              <select
                value={useRandomSize ? 'random' : 'manual'}
                onChange={(e) => setUseRandomSize(e.target.value === 'random')}
                className="w-full border border-slate-700 bg-slate-950 text-white rounded-lg px-3 py-2"
              >
                <option value="manual">Manual Play Mode</option>
                <option value="random">Random Assignment Mode (50–100)</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium mb-2 text-slate-300">
                Task Count
              </label>
              <input
                type="number"
                min={2}
                max={useRandomSize ? 100 : 10}
                value={taskCount}
                onChange={(e) => setTaskCount(Number(e.target.value))}
                disabled={useRandomSize}
                className="w-full border border-slate-700 bg-slate-950 text-white rounded-lg px-3 py-2 disabled:opacity-50"
                placeholder={useRandomSize ? 'Randomized by backend' : 'Enter task count'}
              />
              <p className="text-xs text-slate-500 mt-1">
                {useRandomSize
                  ? 'Backend will generate a task count between 50 and 100.'
                  : 'Use a small value like 2 to 10 so the game is playable.'}
              </p>
            </div>

            <div className="flex items-end">
              <div className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-sm text-slate-400">
                {useRandomSize
                  ? 'Random mode matches the assignment requirement.'
                  : 'Manual mode is better for actually playing the game.'}
              </div>
            </div>

            <div className="md:col-span-2 flex gap-3 flex-wrap">
              <button
                onClick={handleStart}
                disabled={loading}
                className="rounded-xl bg-blue-600 hover:bg-blue-500 text-white px-4 py-2 font-semibold disabled:opacity-50"
              >
                {loading ? 'Loading...' : 'Start Round'}
              </button>

              <button
                onClick={handleSubmit}
                disabled={loading || !round || hasSubmitted}
                className="rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white px-4 py-2 font-semibold disabled:opacity-50"
              >
                Submit Answer
              </button>

              {hasSubmitted && (
                <button
                  onClick={handleRetry}
                  disabled={loading}
                  className="rounded-xl bg-slate-700 hover:bg-slate-600 text-white px-4 py-2 font-semibold disabled:opacity-50"
                >
                  Retry
                </button>
              )}
            </div>
          </div>

          {error && <div className="text-red-400 font-medium">{error}</div>}

          {round && (
            <>
              <div className="bg-slate-900 rounded-2xl border border-slate-800 p-4 space-y-3">
                <div className="text-sm text-slate-400">
                  Generated task count for this round:{' '}
                  <span className="font-semibold text-white">{round.taskCount}</span>
                </div>

                <div>
                  <div className="font-semibold mb-2 text-white">
                    Choose the minimum total cost
                  </div>
                  <AnswerOptions
                    options={round.answerChoices || []}
                    selected={selectedAnswer}
                    onSelect={setSelectedAnswer}
                    disabled={hasSubmitted}
                  />
                </div>
              </div>

              <ResultBanner result={result} correctAnswer={correctAnswer} />

              <div className="bg-slate-900 rounded-2xl border border-slate-800 p-4 space-y-4">
                {hasSubmitted && (
                  <div className="grid md:grid-cols-2 gap-4">
                    <div className="rounded-xl bg-slate-800 p-3 border border-slate-700">
                      <div className="font-semibold text-white">Hungarian Algorithm</div>
                      <div className="text-slate-300">Answer: {round.hungarianAnswer}</div>
                      <div className="text-slate-400">Time: {round.hungarianTimeNanos} ns</div>
                    </div>

                    <div className="rounded-xl bg-slate-800 p-3 border border-slate-700">
                      <div className="font-semibold text-white">Min-Cost Max-Flow</div>
                      <div className="text-slate-300">Answer: {round.minCostFlowAnswer}</div>
                      <div className="text-slate-400">Time: {round.minCostFlowTimeNanos} ns</div>
                    </div>
                  </div>
                )}

                <CostMatrixView matrix={round.costMatrix} />
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}