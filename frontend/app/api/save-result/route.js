import { NextResponse } from 'next/server';

export async function POST(request) {
  try {
    const data = await request.json();
    
    // Example: Using a mock DB call or Prisma
    console.log("Saving to DB:", {
      player: data.name,
      answer: data.flow,
      algo_times: { edmondsKarp: data.ekTime, fordFulkerson: data.ffTime },
      timestamp: new Date()
    });

    return NextResponse.json({ success: true });
  } catch (error) {
    return NextResponse.json({ error: "Validation Failed" }, { status: 400 });
  }
}