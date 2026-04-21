'use client';

import { useState, useEffect, useMemo } from 'react';
import Link from 'next/link';
import { Trophy, Medal, ArrowLeft, CheckCircle2, XCircle, Clock, Move } from 'lucide-react';
import styles from './leaderboard.module.css';

export default function LeaderboardPage() {
  const [leaderboardData, setLeaderboardData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchLeaderboard() {
      try {
        const response = await fetch('http://localhost:8080/api/knight-tour/leaderboard');
        if (!response.ok) {
          throw new Error('Failed to fetch leaderboard data');
        }
        const data = await response.json();
        setLeaderboardData(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    fetchLeaderboard();
  }, []);

  const formatTime = (millis) => {
    if (!millis) return '0 s';
    const totalSeconds = millis / 1000;
    if (totalSeconds < 60) {
      return `${totalSeconds.toFixed(1)} s`;
    }
    const mins = Math.floor(totalSeconds / 60);
    const secs = Math.floor(totalSeconds % 60);
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    }).format(date);
  };

  const top3 = useMemo(() => leaderboardData.slice(0, 3), [leaderboardData]);
  const restOfPlayers = useMemo(() => leaderboardData.slice(3), [leaderboardData]);

  if (loading) {
    return (
      <div className={styles.pageContainer}>
        <div className={styles.loadingContainer}>
          <div className={styles.spinner}></div>
          <p>Loading leaderboard...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.pageContainer}>
        <div className={styles.errorContainer}>
          <h2>Oops! Something went wrong.</h2>
          <p>{error}</p>
          <Link href="/knights-tour" className={styles.backButton}>
            <ArrowLeft size={16} /> Back to Game
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.pageContainer}>
      <header className={styles.header}>
        <Link href="/knights-tour" className={styles.backButton}>
          <ArrowLeft size={16} /> Back to Game
        </Link>
        <h1 className={styles.title}>Knight&apos;s Tour Leaderboard</h1>
        <p className={styles.subtitle}>
          Top players ranked by completion status, speed, and performance
        </p>
      </header>

      {leaderboardData.length === 0 ? (
        <div className={styles.emptyState}>
          <Trophy size={48} className={styles.emptyIcon} />
          <h3>No leaderboard results yet</h3>
          <p>Be the first to complete the Knight&apos;s Tour and claim the top spot!</p>
        </div>
      ) : (
        <main className={styles.mainContent}>
          {/* Top 3 Podium Section */}
          <section className={styles.podiumSection}>
            <div className={styles.podiumContainer}>
              {/* 2nd Place */}
              {top3[1] && (
                <div className={`${styles.podiumItem} ${styles.secondPlace}`}>
                  <div className={styles.podiumRank}>
                    <Medal size={28} color="#94a3b8" />
                  </div>
                  <div className={styles.podiumBox}>
                    <span className={styles.rankLabel}>2nd Place</span>
                    <h3 className={styles.podiumName}>{top3[1].playerName}</h3>
                    <div className={styles.podiumStats}>
                      <span>{formatTime(top3[1].timeTakenMillis)}</span>
                      <span>{top3[1].moveCount} moves</span>
                    </div>
                  </div>
                </div>
              )}

              {/* 1st Place */}
              {top3[0] && (
                <div className={`${styles.podiumItem} ${styles.firstPlace}`}>
                  <div className={styles.podiumRank}>
                    <Trophy size={40} color="#fbbf24" />
                  </div>
                  <div className={styles.podiumBox}>
                    <span className={styles.rankLabel}>1st Place</span>
                    <h3 className={styles.podiumName}>{top3[0].playerName}</h3>
                    <div className={styles.podiumStats}>
                      <span>{formatTime(top3[0].timeTakenMillis)}</span>
                      <span>{top3[0].moveCount} moves</span>
                    </div>
                  </div>
                </div>
              )}

              {/* 3rd Place */}
              {top3[2] && (
                <div className={`${styles.podiumItem} ${styles.thirdPlace}`}>
                  <div className={styles.podiumRank}>
                    <Medal size={24} color="#b45309" />
                  </div>
                  <div className={styles.podiumBox}>
                    <span className={styles.rankLabel}>3rd Place</span>
                    <h3 className={styles.podiumName}>{top3[2].playerName}</h3>
                    <div className={styles.podiumStats}>
                      <span>{formatTime(top3[2].timeTakenMillis)}</span>
                      <span>{top3[2].moveCount} moves</span>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </section>

          {/* Rest of the Leaderboard */}
          {restOfPlayers.length > 0 && (
            <section className={styles.listSection}>
              <div className={styles.tableHeader}>
                <div className={styles.colRank}>Rank</div>
                <div className={styles.colPlayer}>Player</div>
                <div className={styles.colStatus}>Status</div>
                <div className={styles.colStats}>Performance</div>
                <div className={styles.colDate}>Date</div>
              </div>

              <div className={styles.listContainer}>
                {restOfPlayers.map((player, index) => {
                  const rank = index + 4;
                  return (
                    <div key={player.id || index} className={styles.listItem}>
                      <div className={styles.colRank}>
                        <span className={styles.rankNumber}>{rank}</span>
                      </div>
                      <div className={styles.colPlayer}>
                        <span className={styles.playerName}>{player.playerName}</span>
                        <span className={styles.boardSize}>{player.boardSize}×{player.boardSize} Board</span>
                      </div>
                      <div className={styles.colStatus}>
                        {player.completed ? (
                          <div className={styles.badgeSuccess}>
                            <CheckCircle2 size={14} /> Completed
                          </div>
                        ) : (
                          <div className={styles.badgeDanger}>
                            <XCircle size={14} /> Failed
                          </div>
                        )}
                        <span className={styles.validationMsg}>{player.validationMessage}</span>
                      </div>
                      <div className={styles.colStats}>
                        <div className={styles.statLine}>
                          <Clock size={14} /> {formatTime(player.timeTakenMillis)}
                        </div>
                        <div className={styles.statLine}>
                          <Move size={14} /> {player.moveCount} moves
                        </div>
                      </div>
                      <div className={styles.colDate}>
                        {formatDate(player.createdAt)}
                      </div>
                    </div>
                  );
                })}
              </div>
            </section>
          )}
        </main>
      )}
    </div>
  );
}
