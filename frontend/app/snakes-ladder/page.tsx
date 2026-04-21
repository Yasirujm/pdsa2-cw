'use client';

import { useState } from 'react';
import {Dices, Swords, User, Grid3X3, Play, Send, RotateCcw, TrendingUp, Activity,} from 'lucide-react';
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

      const response = await submitAnswer({
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
    try {
      setLoading(true);
      setError('');
      setResult(null);
      setCorrectAnswer(undefined);
      setSelectedAnswer(null);

      const data = await createRound(boardSize);
      setRound(data);
    } catch {
      setError('Failed to create new round');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#f6efe3] text-[#1f2937]">
      <div className="mx-auto max-w-7xl px-4 py-6 md:px-8 md:py-10">
        <div className="grid gap-6 lg:grid-cols-[360px_minmax(0,1fr)]">
          <section className="rounded-[28px] border-4 border-[#1f2937] bg-[#fffaf0] p-5 shadow-[8px_8px_0_#1f2937]">
            <div className="rounded-[22px] border-4 border-[#1f2937] bg-[#dff3e4] p-5">
              <div className="inline-flex items-center gap-2 text-xs font-black uppercase tracking-[0.25em] text-[#355070]">
                <Swords size={16} />
                <span>Puzzle Game</span>
              </div>

              <h1 className="mt-3 text-4xl font-black leading-none text-[#1f2937]">
                Snake &
                <br />
                Ladder
              </h1>

              <p className="mt-4 text-sm leading-6 text-[#4b5563]">
                Solve the board by choosing the minimum number of dice throws needed
                to reach the final cell.
              </p>
            </div>

            <div className="mt-5 rounded-[22px] border-4 border-[#1f2937] bg-white p-5">
              <h2 className="text-lg font-black text-[#1f2937]">Player Setup</h2>
              <p className="mt-1 text-sm text-[#6b7280]">
                Start a round and submit your answer.
              </p>

              <div className="mt-5 space-y-4">
                <div>
                  <label className="mb-2 block text-sm font-bold text-[#374151]">
                    Player Name
                  </label>
                  <div className="flex items-center gap-3 rounded-[18px] border-4 border-[#1f2937] bg-[#fffaf0] px-4 py-3">
                    <User size={18} className="text-[#6b7280]" />
                    <input
                      value={playerName}
                      onChange={(e) => setPlayerName(e.target.value)}
                      placeholder="Type your name"
                      className="w-full bg-transparent text-[#111827] outline-none placeholder:text-[#9ca3af]"
                    />
                  </div>
                </div>

                <div>
                  <label className="mb-2 block text-sm font-bold text-[#374151]">
                    Board Size (6 - 12)
                  </label>
                  <div className="flex items-center gap-3 rounded-[18px] border-4 border-[#1f2937] bg-[#fffaf0] px-4 py-3">
                    <Grid3X3 size={18} className="text-[#6b7280]" />
                    <input
                      type="number"
                      min={6}
                      max={12}
                      value={boardSize}
                      onChange={(e) => setBoardSize(Number(e.target.value))}
                      className="w-full bg-transparent text-[#111827] outline-none"
                    />
                  </div>
                </div>

                <button
                  onClick={startRound}
                  disabled={loading}
                  className="inline-flex w-full items-center justify-center gap-2 rounded-[18px] border-4 border-[#1f2937] bg-[#ffb703] px-5 py-3 text-base font-black text-[#1f2937] shadow-[4px_4px_0_#1f2937] transition hover:translate-x-0.5 hover:translate-y-0.5 hover:shadow-none disabled:cursor-not-allowed disabled:opacity-60"
                >
                  <Play size={18} />
                  {loading ? 'Loading...' : 'Start Round'}
                </button>

                <button
                  onClick={handleSubmit}
                  disabled={loading || !round}
                  className="inline-flex w-full items-center justify-center gap-2 rounded-[18px] border-4 border-[#1f2937] bg-[#80ed99] px-5 py-3 text-base font-black text-[#1f2937] shadow-[4px_4px_0_#1f2937] transition hover:translate-x-0.5 hover:translate-y-0.5 hover:shadow-none disabled:cursor-not-allowed disabled:opacity-60"
                >
                  <Send size={18} />
                  Submit Answer
                </button>

                {result && (
                  <button
                    onClick={handleRetry}
                    disabled={loading}
                    className="inline-flex w-full items-center justify-center gap-2 rounded-[18px] border-4 border-[#1f2937] bg-[#cdb4db] px-5 py-3 text-base font-black text-[#1f2937] shadow-[4px_4px_0_#1f2937] transition hover:translate-x-0.5 hover:translate-y-0.5 hover:shadow-none disabled:cursor-not-allowed disabled:opacity-60"
                  >
                    <RotateCcw size={18} />
                    {loading ? 'Loading...' : 'Retry Round'}
                  </button>
                )}
              </div>
            </div>

            <div className="mt-5 grid grid-cols-2 gap-4">
              <div className="rounded-[20px] border-4 border-[#1f2937] bg-[#ffffff] p-4 text-center shadow-[4px_4px_0_#1f2937]">
                <div className="text-xs font-black uppercase tracking-[0.2em] text-[#6b7280]">
                  Round
                </div>
                <div className="mt-2 text-2xl font-black text-[#1f2937]">
                  {round ? round.gameRoundId : '--'}
                </div>
              </div>

              <div className="rounded-[20px] border-4 border-[#1f2937] bg-[#ffffff] p-4 text-center shadow-[4px_4px_0_#1f2937]">
                <div className="text-xs font-black uppercase tracking-[0.2em] text-[#6b7280]">
                  Cells
                </div>
                <div className="mt-2 text-2xl font-black text-[#1f2937]">
                  {boardSize * boardSize}
                </div>
              </div>
            </div>
          </section>

          <section className="space-y-6">
            <div className="rounded-[28px] border-4 border-[#1f2937] bg-[#ffffff] p-5 shadow-[8px_8px_0_#1f2937]">
              <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
                <div>
                  <div className="text-xs font-black uppercase tracking-[0.25em] text-[#355070]">
                    Solve the puzzle
                  </div>
                  <h2 className="mt-2 text-3xl font-black text-[#1f2937]">
                    Answer Board
                  </h2>
                  <p className="mt-2 text-sm leading-6 text-[#6b7280]">
                    Check the board layout and pick the minimum dice throw count.
                  </p>
                </div>

                <div className="rounded-[20px] border-4 border-[#1f2937] bg-[#f1f5f9] px-5 py-4 text-center">
                  <div className="text-xs font-black uppercase tracking-[0.2em] text-[#6b7280]">
                    Status
                  </div>
                  <div className="mt-1 inline-flex items-center gap-2 text-lg font-black text-[#1f2937]">
                    <Dices size={18} />
                    {loading ? 'Loading' : round ? 'Ready' : 'Waiting'}
                  </div>
                </div>
              </div>

              {!round ? (
                <div className="mt-6 rounded-3xl border-4 border-dashed border-[#1f2937] bg-[#fffaf0] px-6 py-12 text-center">
                  <div className="flex justify-center">
                    <div className="rounded-full border-4 border-[#1f2937] bg-white p-4">
                      <Dices size={42} />
                    </div>
                  </div>
                  <h3 className="mt-4 text-2xl font-black text-[#1f2937]">
                    No round started
                  </h3>
                  <p className="mx-auto mt-3 max-w-md text-sm leading-6 text-[#6b7280]">
                    Start a new round from the left panel to generate a board and
                    answer options.
                  </p>
                </div>
              ) : (
                <>
                  <div className="mt-6">
                    <AnswerOptions
                      options={round.answerChoices}
                      selected={selectedAnswer}
                      onSelect={setSelectedAnswer}
                    />
                  </div>

                  <ResultBanner result={result} correctAnswer={correctAnswer} />
                </>
              )}

              {error && (
                <div className="mt-5 rounded-[18px] border-4 border-[#1f2937] bg-[#ffd6d6] px-4 py-3 text-sm font-bold text-[#7f1d1d]">
                  {error}
                </div>
              )}
            </div>

            {round && (
              <div className="rounded-[28px] border-4 border-[#1f2937] bg-[#ffffff] p-5 shadow-[8px_8px_0_#1f2937]">
                <div className="mb-5 flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
                  <div>
                    <div className="text-xs font-black uppercase tracking-[0.25em] text-[#355070]">
                      Visual board
                    </div>
                    <h2 className="mt-2 text-3xl font-black text-[#1f2937]">
                      Game Grid
                    </h2>
                  </div>
                </div>

                <BoardView boardSize={round.boardSize} jumps={round.jumps} />
              </div>
            )}
          </section>
        </div>
      </div>
    </div>
  );
}