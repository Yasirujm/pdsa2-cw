import Link from "next/link";

export default function Home() {
  return (
    <div className="flex gap-2 items-center justify-center min-h-screen">
      <div>
        <Link href="/traffic-game" className="p-4 bg-blue-600 text-white rounded">
          Open Simulation UI
        </Link>
      </div>
      <div>
        <Link href="/snakes-ladder" className="p-4 bg-blue-600 text-white rounded">
          Open Snakes and Ladders
        </Link>
      </div>
      <div>
        <Link href="/minimum-cost" className="p-4 bg-blue-600 text-white rounded">
          Open Minimum Cost
        </Link>
      </div>
      <div>
        <Link href="/knights-tour" className="p-4 bg-blue-600 text-white rounded">
          Open Knight Tour
        </Link>
      </div>
      
    </div>
  );
}