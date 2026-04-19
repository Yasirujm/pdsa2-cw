'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import styles from './knights-tour.module.css';

export default function KnightsTourPage() {
  const [boardSize, setBoardSize] = useState(8);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  // Interactive Game State
  const [playerBoard, setPlayerBoard] = useState([]);
  const [currentRow, setCurrentRow] = useState(null);
  const [currentCol, setCurrentCol] = useState(null);
  const [currentMove, setCurrentMove] = useState(1);
  const [validMoves, setValidMoves] = useState([]);
  const [gameStatus, setGameStatus] = useState('playing');
  const [autoPlaying, setAutoPlaying] = useState(false);
  const [isCalculating, setIsCalculating] = useState(false);
  const [validationResult, setValidationResult] = useState(null);
  const [validationLoading, setValidationLoading] = useState(false);
  const [isValidated, setIsValidated] = useState(false);

  // Player & Timer State
  const [playerName, setPlayerName] = useState('');
  const [elapsedTimeMillis, setElapsedTimeMillis] = useState(0);
  const [startTime, setStartTime] = useState(null);
  const [timerRunning, setTimerRunning] = useState(false);
  const [gameSaved, setGameSaved] = useState(false);
  const [saveResultMessage, setSaveResultMessage] = useState(null);
  const [loadingSave, setLoadingSave] = useState(false);

  // Solve state
  const [greedyLoading, setGreedyLoading] = useState(false);
  const [dfsLoading, setDfsLoading] = useState(false);
  const [greedyResult, setGreedyResult] = useState(null);
  const [dfsResult, setDfsResult] = useState(null);
  const [solveError, setSolveError] = useState(null);

  useEffect(() => {
    let interval;
    if (timerRunning && startTime) {
      interval = setInterval(() => {
        setElapsedTimeMillis(Date.now() - startTime);
      }, 100);
    }
    return () => clearInterval(interval);
  }, [timerRunning, startTime]);

  useEffect(() => {
    async function saveGameResult() {
      if (gameSaved || loadingSave || !result) return;

      let finalCompleted = false;
      let finalMsg = '';

      if (gameStatus === 'lost') {
        finalCompleted = false;
        finalMsg = 'No more valid moves';
      } else if (gameStatus === 'won') {
        if (validationLoading || !validationResult) return;
        finalCompleted = validationResult.isValid;
        finalMsg = validationResult.message;
      } else {
        return;
      }

      setLoadingSave(true);
      try {
        const payload = {
          playerName: playerName.trim() || 'Player',
          boardSize: result.boardSize,
          startRow: result.startRow,
          startCol: result.startCol,
          completed: finalCompleted,
          moveCount: currentMove,
          timeTakenMillis: elapsedTimeMillis,
          validationMessage: finalMsg,
        };

        const response = await fetch('http://localhost:8080/api/knight-tour/save-result', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload),
        });

        if (!response.ok) throw new Error('Save result failed');
        setGameSaved(true);
        setSaveResultMessage('Game result saved successfully');
      } catch (err) {
        setSaveResultMessage('Failed to save result');
      } finally {
        setLoadingSave(false);
      }
    }

    if (gameStatus === 'won' || gameStatus === 'lost') {
      saveGameResult();
    }
  }, [gameStatus, validationResult, validationLoading, gameSaved, loadingSave, result, playerName, elapsedTimeMillis, currentMove]);

  const formatTime = (millis) => {
    const totalSeconds = Math.floor(millis / 1000);
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  };

  async function handleStart() {
    if (!playerName || playerName.trim() === '') {
      setError('Player name is required before starting the game.');
      return;
    }

    setLoading(true);
    setError(null);
    setResult(null);

    // Reset solve results whenever a new game starts
    setGreedyResult(null);
    setDfsResult(null);
    setSolveError(null);

    // Reset game state
    setPlayerBoard([]);
    setCurrentRow(null);
    setCurrentCol(null);
    setCurrentMove(1);
    setValidMoves([]);
    setGameStatus('playing');
    setValidationResult(null);
    setValidationLoading(false);
    setIsValidated(false);

    setElapsedTimeMillis(0);
    setStartTime(null);
    setTimerRunning(false);
    setGameSaved(false);
    setSaveResultMessage(null);
    setLoadingSave(false);

    console.log('Start button clicked');
    console.log('boardSize =', boardSize);

    const controller = new AbortController();
    const timeoutId = setTimeout(() => {
      controller.abort();
    }, 15000);

    try {
      console.log('Sending request to backend...');

      const response = await fetch('http://localhost:8080/api/knight-tour/start', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ boardSize }),
        signal: controller.signal,
      });

      console.log('Response status:', response.status);

      if (!response.ok) {
        const errorText = await response.text();
        console.log('Error response body:', errorText);
        throw new Error(`Server error: ${response.status} ${response.statusText}`);
      }

      const data = await response.json();
      console.log('Received data:', data);
      setResult(data);

      // Initialize interactive game
      const initialBoard = Array.from({ length: boardSize }, () => Array(boardSize).fill(null));
      initialBoard[data.startRow][data.startCol] = 0;
      setPlayerBoard(initialBoard);
      setCurrentRow(data.startRow);
      setCurrentCol(data.startCol);
      setCurrentMove(1);
      setGameStatus('playing');
      setValidMoves(calculateValidMoves(data.startRow, data.startCol, initialBoard, boardSize));
      setStartTime(Date.now());
      setTimerRunning(true);
    } catch (err) {
      console.error('Fetch failed:', err);
      if (err.name === 'AbortError') {
        setError('Request timed out after 15 seconds.');
      } else {
        setError(err.message || 'Something went wrong. Make sure the backend is running.');
      }
    } finally {
      clearTimeout(timeoutId);
      setLoading(false);
      console.log('Request finished');
    }
  }

  async function handleSolve() {
    setGreedyResult(null);
    setDfsResult(null);
    setSolveError(null);
    setGreedyLoading(true);
    setDfsLoading(true);

    const payload = {
      boardSize: result.boardSize,
      startRow: result.startRow,
      startCol: result.startCol,
    };

    try {
      // 1. Solve Greedy
      const greedyResp = await fetch('http://localhost:8080/api/knight-tour/solve-greedy', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!greedyResp.ok) throw new Error('Greedy solve failed');
      const greedyData = await greedyResp.json();
      setGreedyResult(greedyData);
    } catch (err) {
      setSolveError(err.message || 'Greedy failed.');
    } finally {
      setGreedyLoading(false);
    }

    // 2. Solve DFS
    try {
      const dfsResp = await fetch('http://localhost:8080/api/knight-tour/solve-dfs', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!dfsResp.ok) throw new Error('DFS solve failed');
      const dfsData = await dfsResp.json();
      setDfsResult(dfsData);
    } catch (err) {
      if (!solveError) setSolveError(err.message || 'DFS failed.');
    } finally {
      setDfsLoading(false);
    }
  }

  function calculateValidMoves(row, col, board, size) {
    const offsets = [
      [2, 1], [2, -1], [-2, 1], [-2, -1], [1, 2], [1, -2], [-1, 2], [-1, -2]
    ];
    const valid = [];
    for (const [rOff, cOff] of offsets) {
      const newR = row + rOff;
      const newC = col + cOff;
      if (newR >= 0 && newR < size && newC >= 0 && newC < size) {
        if (board[newR][newC] === null) {
          valid.push({ row: newR, col: newC });
        }
      }
    }
    return valid;
  }

  function handleCellClick(row, col) {
    if (gameStatus !== 'playing') return;

    // Check if it's a valid move
    const isValid = validMoves.some(m => m.row === row && m.col === col);
    if (!isValid) return;

    // Make the move
    const newBoard = [...playerBoard];
    newBoard[row] = [...newBoard[row]];
    newBoard[row][col] = currentMove;

    setPlayerBoard(newBoard);
    setCurrentRow(row);
    setCurrentCol(col);

    const nextMove = currentMove + 1;
    setCurrentMove(nextMove);

    // Check win/loss
    if (nextMove === boardSize * boardSize) {
      setGameStatus('won');
      setValidMoves([]);
      setTimerRunning(false);
      if (!isValidated) handleBackendValidation(newBoard);
    } else {
      const nextValidMoves = calculateValidMoves(row, col, newBoard, boardSize);
      setValidMoves(nextValidMoves);
      if (nextValidMoves.length === 0) {
        setGameStatus('lost');
        setTimerRunning(false);
      }
    }
  }

  async function handleBackendValidation(completedBoard) {
    setValidationLoading(true);
    setValidationResult(null);
    setIsValidated(true);

    try {
      const response = await fetch('http://localhost:8080/api/knight-tour/validate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          boardSize: result.boardSize,
          startRow: result.startRow,
          startCol: result.startCol,
          submittedBoard: completedBoard,
        }),
      });

      if (!response.ok) {
        throw new Error('Verification failed');
      }

      const data = await response.json();
      setValidationResult({
        isValid: data.correct,
        message: data.correct
          ? "✅ Valid Knight’s Tour (verified by backend)"
          : "❌ Invalid tour (backend verification failed)",
      });
    } catch (err) {
      setValidationResult({
        isValid: false,
        message: "Validation failed. Backend not reachable.",
      });
    } finally {
      setValidationLoading(false);
    }
  }

  async function handleAutoPlay() {
    setAutoPlaying(true);
    try {
      let dfsBoard = null;

      if (dfsResult && (dfsResult.dfsBoard || dfsResult.board)) {
        dfsBoard = dfsResult.dfsBoard || dfsResult.board;
      } else {
        setIsCalculating(true);
        const response = await fetch('http://localhost:8080/api/knight-tour/solve-dfs', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            boardSize: result.boardSize,
            startRow: result.startRow,
            startCol: result.startCol,
          }),
        });

        if (!response.ok) throw new Error('Solve request failed');
        const data = await response.json();
        dfsBoard = data.dfsBoard || data.board;
        setIsCalculating(false);
      }

      const flatMoves = [];
      const size = result.boardSize;

      for (let r = 0; r < size; r++) {
        for (let c = 0; c < size; c++) {
          flatMoves[dfsBoard[r][c]] = { r, c };
        }
      }

      let freshBoard = Array.from({ length: size }, () => Array(size).fill(null));
      freshBoard[result.startRow][result.startCol] = 0;
      setPlayerBoard(freshBoard);

      for (let i = 1; i < size * size; i++) {
        if (!autoPlaying && i === 1) {
          // We're forcing it anyway once started
        }

        await new Promise(resolve => setTimeout(resolve, 80)); // Animation delay

        const { r, c } = flatMoves[i];
        freshBoard = freshBoard.map(row => [...row]);
        freshBoard[r][c] = i;

        setPlayerBoard(freshBoard);
        setCurrentRow(r);
        setCurrentCol(c);
        setCurrentMove(i + 1);

        if (i === size * size - 1) {
          setGameStatus('won');
          setValidMoves([]);
          setTimerRunning(false);
          if (!isValidated) handleBackendValidation(freshBoard);
        } else {
          const nextValidMoves = calculateValidMoves(r, c, freshBoard, size);
          setValidMoves(nextValidMoves);
          if (nextValidMoves.length === 0) {
            setGameStatus('lost');
            setTimerRunning(false);
          }
        }
      }
    } catch (err) {
      console.error(err);
    } finally {
      setAutoPlaying(false);
      setIsCalculating(false);
    }
  }

  return (
    <main className={styles.page}>
      <Link href="/leaderboard" className={styles.leaderboardNavBtn}>
        🏆 View Global Leaderboard
      </Link>

      <h1 className={styles.title}>♟ Knight&apos;s Tour</h1>
      <p className={styles.subtitle}>
        Select a board size and start the algorithm to see the knight visit every square exactly once.
      </p>

      {/* Controls */}
      <section className={styles.controls}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <label htmlFor="playerName" className={styles.label}>Player Name</label>
          <input
            id="playerName"
            type="text"
            className={styles.playerNameInput}
            placeholder="e.g. David"
            value={playerName}
            onChange={(e) => setPlayerName(e.target.value)}
            disabled={loading || timerRunning || (result && gameStatus === 'playing')}
          />
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <label htmlFor="boardSize" className={styles.label}>
            Board Size
          </label>
          <select
            id="boardSize"
            className={styles.select}
            value={boardSize}
            onChange={(e) => setBoardSize(Number(e.target.value))}
            disabled={loading || timerRunning || (result && gameStatus === 'playing')}
          >
            <option value={8}>8 × 8</option>
            <option value={16}>16 × 16</option>
          </select>
        </div>

        <button
          id="startButton"
          className={styles.startButton}
          onClick={handleStart}
          disabled={loading || greedyLoading || dfsLoading}
          style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}
        >
          {loading ? (
            <>
              <div className={styles.spinner}></div>
              Starting...
            </>
          ) : result ? (
            'Restart Game'
          ) : (
            'Start Game'
          )}
        </button>

        {/* Solve Tour button — only shows after a successful start */}
        {result && (
          <button
            id="solveButton"
            className={styles.solveButton}
            onClick={handleSolve}
            disabled={loading || greedyLoading || dfsLoading}
            style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}
          >
            {greedyLoading || dfsLoading ? (
              <>
                <div className={styles.spinner}></div>
                Solving...
              </>
            ) : (
              'Solve Tour'
            )}
          </button>
        )}
      </section>

      {/* Error */}
      {error && (
        <div className={styles.errorBox} role="alert">
          <strong>Error:</strong> {error}
        </div>
      )}

      {/* Results */}
      {result && (
        <section className={styles.results}>
          <div className={styles.summary}>
            <div className={styles.summaryCard}>
              <span className={styles.summaryLabel}>Board Size</span>
              <span className={styles.summaryValue}>{result.boardSize}</span>
            </div>

            <div className={styles.summaryCard}>
              <span className={styles.summaryLabel}>Start Position</span>
              <span className={styles.summaryValue}>
                Row {result.startRow}, Col {result.startCol}
              </span>
            </div>

            <div className={styles.summaryCard}>
              <span className={styles.summaryLabel}>Status</span>
              <span className={styles.summaryValue}>{result.message}</span>
            </div>
          </div>
        </section>
      )}

      {/* Solve Error */}
      {solveError && (
        <div className={styles.errorBox} role="alert">
          <strong>Solve Error:</strong> {solveError}
        </div>
      )}

      {/* Solve Results Section */}
      {(greedyLoading || dfsLoading || greedyResult || dfsResult) && (
        <section className={styles.results}>
          <hr className={styles.divider} />
          <h2 className={styles.sectionHeading}>Tour Solution</h2>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
            {/* GREEDY SECTION */}
            {greedyLoading && (
              <div className={`${styles.loadingText} ${styles.fadeIn}`}>
                <div className={styles.spinner}></div>
                Solving with Greedy...
              </div>
            )}
            
            {greedyResult && (
              <div className={`${styles.resultCard} ${styles.slideUp}`}>
                <div className={styles.summary}>
                  <div className={styles.summaryCard} style={{ margin: '0 auto 1.5rem auto' }}>
                    <span className={styles.summaryLabel}>Greedy Time</span>
                    <span className={styles.summaryValue}>{greedyResult.greedyTimeTaken ?? greedyResult.timeTakenMillis} ms</span>
                  </div>
                </div>
                <div className={styles.fadeIn}>
                  <BoardSection
                    title="Greedy Solution"
                    board={greedyResult.greedyBoard || greedyResult.board}
                    boardSize={greedyResult.boardSize}
                  />
                </div>
              </div>
            )}

            {/* DFS SECTION */}
            {!greedyLoading && dfsLoading && (
              <div className={`${styles.loadingText} ${styles.pulseText}`}>
                <div className={styles.spinner}></div>
                Solving with DFS... this may take a few seconds
              </div>
            )}

            {dfsResult && (
              <div className={`${styles.resultCard} ${styles.slideUp}`}>
                <div className={styles.summary}>
                  <div className={styles.summaryCard} style={{ margin: '0 auto 1.5rem auto' }}>
                    <span className={styles.summaryLabel}>DFS Time</span>
                    <span className={styles.summaryValue}>{dfsResult.dfsTimeTaken ?? dfsResult.timeTakenMillis} ms</span>
                  </div>
                </div>
                <div className={styles.fadeIn}>
                  <BoardSection
                    title="DFS Solution"
                    board={dfsResult.dfsBoard || dfsResult.board}
                    boardSize={dfsResult.boardSize}
                  />
                </div>
              </div>
            )}
          </div>
        </section>
      )}

      {/* Player Turn Area */}
      {result && playerBoard.length > 0 && (
        <section className={styles.trySection}>
          <hr className={styles.divider} />
          <h2 className={styles.sectionHeading}>Play Knight&apos;s Tour</h2>

          <div className={styles.statusStats}>
            <span>Moves: <strong>{currentMove - 1} / {result.boardSize * result.boardSize - 1}</strong></span>
            <span>Position: <strong>Row {currentRow}, Col {currentCol}</strong></span>
            <span className={styles.timerDisplay}>{formatTime(elapsedTimeMillis)}</span>
            <button
              className={styles.solveButton}
              style={{ fontSize: '0.8rem', padding: '0.2rem 0.5rem', marginLeft: '1rem', display: 'flex', alignItems: 'center' }}
              onClick={handleAutoPlay}
              disabled={autoPlaying || gameStatus !== 'playing'}
            >
              {isCalculating ? (
                <>
                  <div className={styles.spinner}></div>
                  Calculating...
                </>
              ) : autoPlaying ? (
                'Playing...'
              ) : (
                'Auto Play'
              )}
            </button>
          </div>

          <div className={styles.boardWrapper}>
            <div
              className={styles.playerBoard}
              style={{ gridTemplateColumns: `repeat(${result.boardSize}, 1fr)` }}
            >
              {playerBoard.map((row, rowIndex) =>
                row.map((value, colIndex) => {
                  const isLight = (rowIndex + colIndex) % 2 === 0;
                  const isCurrent = rowIndex === currentRow && colIndex === currentCol;
                  const isValid = validMoves.some(m => m.row === rowIndex && m.col === colIndex);

                  let cellClasses = [
                    styles.gameCell,
                    isLight ? styles.cellLight : styles.cellDark
                  ];

                  if (isCurrent) cellClasses.push(styles.gameCellCurrent);
                  if (isValid && gameStatus === 'playing') cellClasses.push(styles.gameCellValid);

                  return (
                    <div
                      key={`${rowIndex}-${colIndex}`}
                      className={cellClasses.join(' ')}
                      onClick={() => handleCellClick(rowIndex, colIndex)}
                    >
                      {value !== null ? value : ''}
                    </div>
                  );
                })
              )}
            </div>
          </div>

          {gameStatus === 'won' && (
            <div className={[styles.gameStatusBox, styles.statusWon].join(' ')}>
              🎉 You completed the Knight&apos;s Tour!
            </div>
          )}
          {gameStatus === 'lost' && (
            <div className={[styles.gameStatusBox, styles.statusLost].join(' ')}>
              ❌ Game Over! No more valid moves.
            </div>
          )}

          {/* Validation Status Card */}
          {(validationLoading || validationResult) && (
            <div className={styles.gameStatusBox} style={{ marginTop: '1rem', background: '#f8fafc', border: '1px solid #e2e8f0', color: '#1e293b' }}>
              {validationLoading ? (
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <div className={styles.spinner} style={{ borderColor: 'rgba(0,0,0,0.1)', borderTopColor: '#3b82f6' }}></div>
                  <span>Validating with backend...</span>
                </div>
              ) : (
                <span style={{ color: validationResult.isValid ? '#166534' : '#b91c1c', fontWeight: 'bold' }}>
                  {validationResult.message}
                </span>
              )}
            </div>
          )}

          {/* Save Result Status Card */}
          {(loadingSave || saveResultMessage) && (
            <div className={styles.saveSummaryBox}>
              {loadingSave ? (
                <>
                  <div className={styles.spinner} style={{ borderColor: 'rgba(0,0,0,0.1)', borderTopColor: '#3b82f6' }}></div>
                  <span>Saving game result...</span>
                </>
              ) : (
                <span style={{ color: gameSaved ? '#0284c7' : '#b91c1c', fontWeight: 'bold' }}>
                  {saveResultMessage}
                </span>
              )}
            </div>
          )}
        </section>
      )}
    </main>
  );
}

/** Renders one labelled board. */
function BoardSection({ title, board, boardSize }) {
  return (
    <div className={styles.boardSection}>
      <h2 className={styles.boardTitle}>{title}</h2>
      <div className={styles.boardWrapper}>
        <div
          className={styles.board}
          style={{ gridTemplateColumns: `repeat(${boardSize}, 1fr)` }}
        >
          {board.map((row, rowIndex) =>
            row.map((value, colIndex) => {
              const isLight = (rowIndex + colIndex) % 2 === 0;
              const isStart = value === 0;
              return (
                <div
                  key={`${rowIndex}-${colIndex}`}
                  className={[
                    styles.cell,
                    isStart ? styles.cellStart : isLight ? styles.cellLight : styles.cellDark,
                  ].join(' ')}
                >
                  {value}
                </div>
              );
            })
          )}
        </div>
      </div>
    </div>
  );
}
