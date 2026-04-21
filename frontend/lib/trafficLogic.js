
export function edmondsKarp(edges, nodes, source, sink) {
  let startTime = performance.now();
  let graph = {};
  let originalGraph = {};

 
  nodes.forEach(n => { graph[n] = {}; originalGraph[n] = {}; });
  edges.forEach(({ from, to, capacity }) => {
    graph[from][to] = capacity;
    graph[to][from] = 0; 
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

 
  let edgeFlows = {};
  edges.forEach(({ from, to }) => {
   
    edgeFlows[`${from}-${to}`] = originalGraph[from][to] - graph[from][to];
  });

  return { flow: maxFlow, time: performance.now() - startTime, edgeFlows };
}


export function fordFulkerson(edges, nodes, source, sink) {

  return { time: 0 }; 
}