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
    } catch {
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
    } catch {
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
    } catch {
      setError('Failed to create new round');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[radial-gradient(circle_at_top,_#3b1d0f_0%,_#1e1b2e_38%,_#0f172a_100%)] text-slate-100 px-4 py-8 md:px-6 lg:px-8">
      <div className="mx-auto max-w-7xl space-y-6">
        <div className="overflow-hidden rounded-[28px] border border-white/10 bg-white/5 shadow-[0_20px_80px_rgba(0,0,0,0.35)] backdrop-blur-xl">
          <div className="grid lg:grid-cols-[360px_1fr]">
            <aside className="space-y-6 border-b border-white/10 bg-slate-950/40 p-6 lg:border-b-0 lg:border-r md:p-7">
              <div className="space-y-3">
                <div>
                  <h1 className="text-3xl font-bold leading-tight text-white">
                    Minimum Cost Solver
                  </h1>
                  <p className="mt-2 text-sm leading-6 text-slate-300">
                    Generate a task-assignment matrix and predict the minimum total cost.
                  </p>
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div className="rounded-2xl border border-white/10 bg-white/5 p-4">
                  <div className="text-xs uppercase tracking-wide text-slate-400">Difficulty</div>
                  <div className="mt-2 text-sm font-semibold text-rose-300">
                    {useRandomSize ? 'Random' : 'Manual'}
                  </div>
                </div>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="mb-2 block text-sm font-medium text-slate-300">
                    Player Name
                  </label>
                  <input
                    value={playerName}
                    onChange={(e) => setPlayerName(e.target.value)}
                    placeholder="Enter your name"
                    className="w-full rounded-2xl border border-white/10 bg-slate-900/70 px-4 py-3 text-white outline-none transition focus:border-amber-400/60 focus:ring-2 focus:ring-amber-500/20"
                  />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-medium text-slate-300">
                    Play Mode
                  </label>
                  <select
                    value={useRandomSize ? 'random' : 'manual'}
                    onChange={(e) => setUseRandomSize(e.target.value === 'random')}
                    className="w-full rounded-2xl border border-white/10 bg-slate-900/70 px-4 py-3 text-white outline-none transition focus:border-rose-400/60 focus:ring-2 focus:ring-rose-500/20"
                  >
                    <option value="manual">Manual Play Mode</option>
                    <option value="random">Random Assignment Mode (50–100)</option>
                  </select>
                </div>

                <div>
                  <label className="mb-2 block text-sm font-medium text-slate-300">
                    Task Count
                  </label>
                  <input
                    type="number"
                    min={2}
                    max={useRandomSize ? 100 : 10}
                    value={taskCount}
                    onChange={(e) => setTaskCount(Number(e.target.value))}
                    disabled={useRandomSize}
                    placeholder={useRandomSize ? 'Generated automatically' : 'Enter task count'}
                    className="w-full rounded-2xl border border-white/10 bg-slate-900/70 px-4 py-3 text-white outline-none transition disabled:cursor-not-allowed disabled:opacity-50 focus:border-teal-400/60 focus:ring-2 focus:ring-teal-500/20"
                  />
                  <p className="mt-2 text-xs leading-5 text-slate-400">
                    {useRandomSize
                      ? 'The backend will generate a matrix between 50 and 100 tasks.'
                      : 'Keep it between 2 and 10 for a playable round.'}
                  </p>
                </div>
              </div>

              <div className="space-y-3">
                <button
                  onClick={handleStart}
                  disabled={loading}
                  className="w-full rounded-2xl bg-amber-500 px-4 py-3 font-semibold text-slate-950 transition hover:bg-amber-400 disabled:opacity-50"
                >
                  {loading ? 'Loading...' : 'Generate Round'}
                </button>

                <button
                  onClick={handleSubmit}
                  disabled={loading || !round || hasSubmitted}
                  className="w-full rounded-2xl bg-rose-500 px-4 py-3 font-semibold text-white transition hover:bg-rose-400 disabled:opacity-50"
                >
                  Submit Guess
                </button>

                {hasSubmitted && (
                  <button
                    onClick={handleRetry}
                    disabled={loading}
                    className="w-full rounded-2xl border border-white/10 bg-teal-500/10 px-4 py-3 font-semibold text-teal-200 transition hover:bg-teal-500/20 disabled:opacity-50"
                  >
                    Play Another Round
                  </button>
                )}
              </div>

              {error && (
                <div className="rounded-2xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm font-medium text-red-300">
                  {error}
                </div>
              )}
            </aside>

            <main className="space-y-6 p-6 md:p-7">
              {!round ? (
                <div className="flex min-h-[520px] items-center justify-center rounded-[24px] border border-dashed border-white/10 bg-slate-950/20 p-8 text-center">
                  <div className="max-w-md">
                    <div className="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-2xl bg-amber-500/10 text-3xl">
                      🧩
                    </div>
                    <h2 className="text-2xl font-bold text-white">Ready to Start?</h2>
                    <p className="mt-3 text-slate-400">
                      Configure the round on the left, generate a matrix, then choose the minimum
                      total assignment cost from the given options.
                    </p>
                  </div>
                </div>
              ) : (
                <>
                  <div className="grid gap-4 md:grid-cols-3">
                    <div className="rounded-3xl border border-white/10 bg-gradient-to-br from-amber-500/15 to-amber-500/5 p-5">
                      <div className="text-sm text-amber-200">Generated Tasks</div>
                      <div className="mt-2 text-3xl font-bold text-white">{round.taskCount}</div>
                    </div>

                    <div className="rounded-3xl border border-white/10 bg-gradient-to-br from-rose-500/15 to-rose-500/5 p-5">
                      <div className="text-sm text-rose-200">Answer Choices</div>
                      <div className="mt-2 text-3xl font-bold text-white">
                        {round.answerChoices?.length ?? 0}
                      </div>
                    </div>

                    <div className="rounded-3xl border border-white/10 bg-gradient-to-br from-teal-500/15 to-teal-500/5 p-5">
                      <div className="text-sm text-teal-200">Round Status</div>
                      <div className="mt-2 text-lg font-bold text-white">
                        {hasSubmitted ? 'Submitted' : 'Waiting for answer'}
                      </div>
                    </div>
                  </div>

                  <div className="space-y-5 rounded-[24px] border border-white/10 bg-slate-950/30 p-5 md:p-6">
                    <div className="flex flex-col gap-2 md:flex-row md:items-end md:justify-between">
                      <div>
                        <h2 className="text-xl font-bold text-white">Pick the Best Total Cost</h2>
                        <p className="mt-1 text-sm text-slate-400">
                          Select the option you believe gives the minimum assignment cost.
                        </p>
                      </div>

                      <div className="rounded-full border border-white/10 bg-white/5 px-4 py-2 text-sm text-slate-300">
                        Selected:{' '}
                        <span className="font-semibold text-amber-300">
                          {selectedAnswer ?? 'None'}
                        </span>
                      </div>
                    </div>

                    <AnswerOptions
                      options={round.answerChoices || []}
                      selected={selectedAnswer}
                      onSelect={setSelectedAnswer}
                      disabled={hasSubmitted}
                    />
                  </div>

                  <ResultBanner result={result} correctAnswer={correctAnswer} />

                  {hasSubmitted && (
                    <div className="grid gap-4 md:grid-cols-2">
                      <div className="rounded-[24px] border border-amber-500/20 bg-amber-500/5 p-5">
                        <div className="text-sm font-medium uppercase tracking-wide text-amber-300">
                          Hungarian Algorithm
                        </div>
                        <div className="mt-3 text-2xl font-bold text-white">
                          {round.hungarianAnswer}
                        </div>
                        <div className="mt-2 text-sm text-slate-300">
                          Runtime: {round.hungarianTimeNanos} ns
                        </div>
                      </div>

                      <div className="rounded-[24px] border border-rose-500/20 bg-rose-500/5 p-5">
                        <div className="text-sm font-medium uppercase tracking-wide text-rose-300">
                          Min-Cost Max-Flow
                        </div>
                        <div className="mt-3 text-2xl font-bold text-white">
                          {round.minCostFlowAnswer}
                        </div>
                        <div className="mt-2 text-sm text-slate-300">
                          Runtime: {round.minCostFlowTimeNanos} ns
                        </div>
                      </div>
                    </div>
                  )}

                  <CostMatrixView matrix={round.costMatrix} />
                </>
              )}
            </main>
          </div>
        </div>
      </div>
    </div>
  );
}