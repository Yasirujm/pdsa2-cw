import Link from "next/link";
import {
  Activity,
  ArrowRight,
  Crown,
  Route,
  TrafficCone,
  Truck,
  ChessKnight
} from "lucide-react";

const games = [
  {
    title: "Traffic Game",
    description: "Simulate traffic flow and explore system behavior visually.",
    href: "/traffic-game",
    icon: TrafficCone,
    accent: "from-cyan-500/20 to-blue-500/20",
    border: "border-cyan-500/20",
    iconBg: "bg-cyan-500/10",
    iconColor: "text-cyan-400",
  },
  {
    title: "Snakes and Ladders",
    description: "Play the classic board game with algorithm-powered logic.",
    href: "/snakes-ladder",
    icon: Route,
    accent: "from-violet-500/20 to-fuchsia-500/20",
    border: "border-violet-500/20",
    iconBg: "bg-violet-500/10",
    iconColor: "text-violet-400",
  },
  {
    title: "Minimum Cost",
    description: "Solve optimization problems and compare smart solutions.",
    href: "/minimum-cost",
    icon: Truck,
    accent: "from-emerald-500/20 to-teal-500/20",
    border: "border-emerald-500/20",
    iconBg: "bg-emerald-500/10",
    iconColor: "text-emerald-400",
  },
  {
    title: "Knight’s Tour",
    description: "Visualize the knight’s path across the chessboard.",
    href: "/knights-tour",
    icon: ChessKnight,
    accent: "from-amber-500/20 to-orange-500/20",
    border: "border-amber-500/20",
    iconBg: "bg-amber-500/10",
    iconColor: "text-amber-400",
  },
  {
    title: "8 Queens Puzzle",
    description: "Place queens strategically and explore valid solutions.",
    href: "/queens",
    icon: Crown,
    accent: "from-pink-500/20 to-rose-500/20",
    border: "border-pink-500/20",
    iconBg: "bg-pink-500/10",
    iconColor: "text-pink-400",
  },
];

export default function Home() {
  return (
    <main className="min-h-screen bg-[#0a0f1f] text-white">
      <div className="relative overflow-hidden">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_left,rgba(59,130,246,0.15),transparent_30%),radial-gradient(circle_at_bottom_right,rgba(168,85,247,0.12),transparent_30%)]" />
        <div className="absolute inset-0 opacity-20 bg-[linear-gradient(to_right,#ffffff08_1px,transparent_1px),linear-gradient(to_bottom,#ffffff08_1px,transparent_1px)] bg-size-[32px_32px]" />

        <div className="relative mx-auto max-w-7xl px-6 py-10 md:px-10 md:py-14">
          <div className="mb-10 flex flex-col gap-6 md:mb-14 md:flex-row md:items-end md:justify-between">
            <div className="max-w-2xl">

              <h1 className="text-4xl font-bold tracking-tight text-white md:text-6xl">
                Game Control Center
              </h1>

              <p className="mt-4 text-base leading-7 text-slate-400 md:text-lg">
                Launch your algorithm-based games and simulations
              </p>
            </div>
          </div>

          <section className="grid gap-6 sm:grid-cols-2 xl:grid-cols-3">
            {games.map((game) => {
              const Icon = game.icon;

              return (
                <Link
                  key={game.href}
                  href={game.href}
                  className={`group relative overflow-hidden rounded-3xl border ${game.border} bg-white/5 p-6 backdrop-blur-xl transition-all duration-300 hover:-translate-y-1 hover:border-white/20 hover:bg-white/10 hover:shadow-2xl`}
                >
                  <div
                    className={`absolute inset-0 bg-linear-to-br ${game.accent} opacity-70`}
                  />
                  <div className="relative z-10">
                    <div
                      className={`mb-5 flex h-14 w-14 items-center justify-center rounded-2xl ${game.iconBg} ring-1 ring-white/10`}
                    >
                      <Icon className={`h-7 w-7 ${game.iconColor}`} />
                    </div>

                    <h2 className="text-xl font-semibold text-white">
                      {game.title}
                    </h2>

                    <p className="mt-3 text-sm leading-6 text-slate-300">
                      {game.description}
                    </p>

                    <div className="mt-6 flex items-center justify-between">
                      <span className="text-sm font-medium text-slate-200">
                        Open Module
                      </span>
                      <div className="rounded-full border border-white/10 bg-white/10 p-2 transition-transform duration-300 group-hover:translate-x-1">
                        <ArrowRight className="h-4 w-4 text-white" />
                      </div>
                    </div>
                  </div>
                </Link>
              );
            })}
          </section>
        </div>
      </div>
    </main>
  );
}