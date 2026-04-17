// 1. Edmonds-Karp (BFS based)
export function edmondsKarp(edges, nodes, source, sink) {
  let startTime = performance.now();
  let graph = {};
  let originalGraph = {}; // New: We need to remember the starting capacities

  // Setup graphs
  nodes.forEach(n => { graph[n] = {}; originalGraph[n] = {}; });
  edges.forEach(({ from, to, capacity }) => {
    graph[from][to] = capacity;
    graph[to][from] = 0; // Reverse path for residual graph
    originalGraph[from][to] = capacity;
  });

  let maxFlow = 0;
  let parent = {};

  const bfs = () => {
    parent = {};
    let queue = [source];
    parent[source] = null;
    while (queue.length > 0) {
      let u = queue.shift();
      for (let v in graph[u]) {
        if (parent[v] === undefined && graph[u][v] > 0) {
          parent[v] = u;
          queue.push(v);
          if (v === sink) return true;
        }
      }
    }
    return false;
  };

  while (bfs()) {
    let pathFlow = Infinity;
    for (let v = sink; v !== source; v = parent[v]) {
      let u = parent[v];
      pathFlow = Math.min(pathFlow, graph[u][v]);
    }
    for (let v = sink; v !== source; v = parent[v]) {
      let u = parent[v];
      graph[u][v] -= pathFlow;
      graph[v][u] += pathFlow;
    }
    maxFlow += pathFlow;
  }

  // New: Calculate exactly how much flow went down each specific edge
  let edgeFlows = {};
  edges.forEach(({ from, to }) => {
    // Flow is the original capacity minus whatever is left over (residual)
    edgeFlows[`${from}-${to}`] = originalGraph[from][to] - graph[from][to];
  });

  return { flow: maxFlow, time: performance.now() - startTime, edgeFlows };
}

// 2. Ford-Fulkerson (DFS based) - Kept for your database time-tracking
export function fordFulkerson(edges, nodes, source, sink) {
  // ... (You can leave your existing Ford-Fulkerson code here, we only need EK for the visual path)
  return { time: 0 }; 
}