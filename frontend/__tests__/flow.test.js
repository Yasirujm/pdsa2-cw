import { edmondsKarp } from '../lib/trafficLogic';

describe('Traffic Flow Algorithm', () => {
  test('calculates correct max flow for a simple network', () => {
    const nodes = ['A', 'B', 'T'];
    const edges = [
      { from: 'A', to: 'B', capacity: 10 },
      { from: 'B', to: 'T', capacity: 5 }
    ];
    const result = edmondsKarp(edges, nodes, 'A', 'T');
    expect(result.flow).toBe(5);
  });
});