import Link from "next/link";

export default function Home() {
  return (
    <div className="flex items-center justify-center min-h-screen">
      <Link href="/traffic-game" className="p-4 bg-blue-600 text-white rounded">
        Open Simulation UI
      </Link>
    </div>
  );
}