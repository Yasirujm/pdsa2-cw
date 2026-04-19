"use client";

import { useState } from 'react';

const BACKEND_BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080';

const NODE_POSITIONS = {
  A: { x: 50, y: 200 }, B: { x: 200, y: 80 }, C: { x: 200, y: 200 },
  D: { x: 200, y: 320 }, E: { x: 380, y: 140 }, F: { x: 380, y: 260 },
  G: { x: 550, y: 140 }, H: { x: 550, y: 260 }, T: { x: 700, y: 200 },
};

export default function TrafficGame() {
  const [network, setNetwork] = useState([]);
  const [userInput, setUserInput] = useState("");
  const [userName, setUserName] = useState("");
  const [gameStatus, setGameStatus] = useState("IDLE");
  const [correctAnswer, setCorrectAnswer] = useState(null);
  const [roundId, setRoundId] = useState(null);
  const [finalFlows, setFinalFlows] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const startRound = async () => {
    setIsLoading(true);
    setErrorMessage("");

    try {
      const response = await fetch(`${BACKEND_BASE_URL}/api/traffic-game/rounds`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ minCapacity: 5, maxCapacity: 15 }),
      });

      if (!response.ok) {
        const errorBody = await response.json().catch(() => ({}));
        throw new Error(errorBody.error || 'Failed to generate network map');
      }

      const data = await response.json();
      setNetwork(data.edges || []);
      setRoundId(data.roundId);
      setGameStatus("PLAYING");
      setUserInput("");
      setCorrectAnswer(null);
      setFinalFlows({});
    } catch (error) {
      setErrorMessage(error.message || 'Cannot connect to backend server');
      setGameStatus("IDLE");
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = async () => {
    if (!userName || !userInput) {
      return alert("Please enter your name and flow estimate!");
    }

    if (!roundId) {
      return alert("Round is missing. Please generate a new network map.");
    }

    const parsedInput = parseInt(userInput, 10);
    if (Number.isNaN(parsedInput)) {
      return alert("Please enter a valid number.");
    }

    setIsLoading(true);
    setErrorMessage("");

    try {
      const response = await fetch(`${BACKEND_BASE_URL}/api/traffic-game/answers`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          roundId,
          playerName: userName,
          estimatedMaxFlow: parsedInput,
        }),
      });

      if (!response.ok) {
        const errorBody = await response.json().catch(() => ({}));
        throw new Error(errorBody.error || 'Failed to submit answer');
      }

      const data = await response.json();
      setCorrectAnswer(data.actualMaxFlow);

      const flowMap = {};
      (data.edgeFlows || []).forEach((edge) => {
        flowMap[`${edge.from}-${edge.to}`] = edge.flow;
      });
      setFinalFlows(flowMap);

      setGameStatus(data.correct ? "WON" : "LOST");
    } catch (error) {
      setErrorMessage(error.message || 'Cannot connect to backend server');
    } finally {
      setIsLoading(false);
    }
  };

  const showResults = gameStatus === "WON" || gameStatus === "LOST";

  return (
    <div className="min-h-screen bg-slate-950 text-slate-200 flex flex-col items-center py-12 px-4 font-sans">
      <div className="max-w-4xl w-full bg-slate-900 border border-slate-800 p-8 rounded-3xl shadow-2xl">
        
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold mb-2 text-white">Traffic Network Simulator</h1>
          <p className="text-slate-400">Calculate maximum flow from <span className="text-blue-400 font-bold">A</span> to <span className="text-emerald-400 font-bold">T</span>.</p>
          {errorMessage && (
            <p className="text-sm text-red-400 mt-3">{errorMessage}</p>
          )}
        </div>
        
        {gameStatus === "IDLE" ? (
          <div className="flex justify-center mt-10">
            <button onClick={startRound} disabled={isLoading} className="px-8 py-4 bg-blue-600 hover:bg-blue-500 rounded-full font-bold text-white transition-all disabled:opacity-60 disabled:cursor-not-allowed">
              {isLoading ? "Generating..." : "Generate Network Map"}
            </button>
          </div>
        ) : (
          <div className="space-y-8 animate-in fade-in duration-500">
            
            {/* NETWORK GRAPH */}
            <div className="w-full overflow-x-auto bg-slate-950 border border-slate-800 rounded-2xl p-4 relative">
              <svg viewBox="0 0 750 400" className="w-full h-auto min-w-[600px]">
                <defs>
                  <marker id="arrowhead" markerWidth="10" markerHeight="7" refX="24" refY="3.5" orient="auto">
                    <polygon points="0 0, 10 3.5, 0 7" fill="#475569" />
                  </marker>
                  <marker id="arrowhead-active" markerWidth="10" markerHeight="7" refX="24" refY="3.5" orient="auto">
                    <polygon points="0 0, 10 3.5, 0 7" fill="#10b981" />
                  </marker>
                </defs>

                {/* EDGES */}
                {network.map((edge, i) => {
                  const from = NODE_POSITIONS[edge.from];
                  const to = NODE_POSITIONS[edge.to];
                  const midX = (from.x + to.x) / 2;
                  const midY = (from.y + to.y) / 2;
                  
                  
                  const flowUsed = finalFlows[`${edge.from}-${edge.to}`] || 0;
                  const isActive = showResults && flowUsed > 0;
                  const lineOpacity = showResults && !isActive ? "opacity-30" : "opacity-100";

                  return (
                    <g key={`edge-${i}`} className={`transition-all duration-1000 ${lineOpacity}`}>
                      <line 
                        x1={from.x} y1={from.y} x2={to.x} y2={to.y} 
                        stroke={isActive ? "#10b981" : "#475569"} 
                        strokeWidth={isActive ? "5" : "3"} 
                        markerEnd={isActive ? "url(#arrowhead-active)" : "url(#arrowhead)"} 
                      />
                      <rect x={midX - 20} y={midY - 12} width="40" height="20" fill="#0f172a" rx="4" />
                      <text x={midX} y={midY + 3} fill={isActive ? "#10b981" : "#cbd5e1"} fontSize="12" fontWeight="bold" textAnchor="middle">
                        {showResults ? `${flowUsed}/${edge.capacity}` : edge.capacity}
                      </text>
                    </g>
                  );
                })}

                {/* NODES */}
                {Object.entries(NODE_POSITIONS).map(([node, pos]) => (
                  <g key={`node-${node}`}>
                    <circle cx={pos.x} cy={pos.y} r="18" fill={node === 'A' ? '#3b82f6' : node === 'T' ? '#10b981' : '#1e293b'} stroke="#334155" strokeWidth="2" />
                    <text x={pos.x} y={pos.y + 5} fill="white" fontSize="14" fontWeight="bold" textAnchor="middle">{node}</text>
                  </g>
                ))}
              </svg>
            </div>
            
            {/* INPUTS */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <input type="text" placeholder="Player Name" value={userName} disabled={showResults} onChange={e => setUserName(e.target.value)} className="bg-slate-950 border border-slate-700 text-white p-4 rounded-xl outline-none disabled:opacity-50" />
              <input type="number" placeholder="Enter Max Flow" value={userInput} disabled={showResults} onChange={e => setUserInput(e.target.value)} className="bg-slate-950 border border-slate-700 text-emerald-400 font-bold p-4 rounded-xl outline-none disabled:opacity-50" />
            </div>
            
            {/* ACTIONS & FEEDBACK */}
            {gameStatus === "PLAYING" && (
              <button onClick={handleSubmit} disabled={isLoading} className="w-full py-4 bg-blue-600 hover:bg-blue-500 text-white font-bold rounded-xl transition-all disabled:opacity-60 disabled:cursor-not-allowed">
                {isLoading ? "Submitting..." : "Submit Answer"}
              </button>
            )}

            {gameStatus === "WON" && (
              <div className="p-6 bg-emerald-900/30 border border-emerald-500/50 rounded-xl text-center">
                <h3 className="text-emerald-400 font-bold text-xl mb-2">Victory! Calculation Correct.</h3>
                <p className="text-slate-300">The graph above now shows the active routes in <span className="text-emerald-400 font-bold">Green</span>.</p>
                <button onClick={startRound} className="mt-4 px-6 py-2 bg-emerald-600 hover:bg-emerald-500 text-white rounded-lg">Play Again</button>
              </div>
            )}

            {gameStatus === "LOST" && (
              <div className="p-6 bg-red-900/30 border border-red-500/50 rounded-xl text-center">
                <h3 className="text-red-400 font-bold text-xl mb-2">Incorrect Calculation</h3>
                <p className="text-slate-300">You guessed <span className="font-bold text-white">{userInput}</span>, but the actual maximum flow is <span className="font-bold text-emerald-400 bg-emerald-900/50 px-2 py-1 rounded">{correctAnswer}</span>.</p>
                <p className="text-slate-400 text-sm mt-2">Check the graph above. The <span className="text-emerald-400 font-bold">Green</span> paths show the exact flow distribution needed.</p>
                <button onClick={startRound} className="mt-6 px-6 py-2 bg-slate-700 hover:bg-slate-600 text-white rounded-lg">Generate New Graph</button>
              </div>
            )}

          </div>
        )}
      </div>
    </div>
  );
}