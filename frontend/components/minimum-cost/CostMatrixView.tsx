'use client';

type Props = {
  matrix: number[][];
};

export default function CostMatrixView({ matrix }: Props) {
  const maxPreview = 10;
  const previewRows = matrix.slice(0, maxPreview);
  const previewCols = matrix[0]?.slice(0, maxPreview) ?? [];

  return (
    <div className="overflow-hidden rounded-[24px] border border-white/10 bg-slate-950/30">
      <div className="flex flex-col gap-3 border-b border-white/10 px-5 py-4 md:flex-row md:items-center md:justify-between">
        <div>
          <h3 className="text-lg font-bold text-white">Cost Matrix Preview</h3>
          <p className="text-sm text-slate-400">
            Review the first part of the generated task-to-employee cost table.
          </p>
        </div>

        <div className="inline-flex items-center rounded-full border border-white/10 bg-rose-500/10 px-3 py-1 text-xs font-medium text-rose-200">
          Preview limit: 10 × 10
        </div>
      </div>

      <div className="overflow-auto">
        <table className="min-w-full border-collapse text-sm">
          <thead>
            <tr className="bg-white/5 text-slate-300">
              <th className="border-b border-white/10 px-4 py-3 text-left font-semibold">
                Task / Employee
              </th>
              {previewCols.map((_, index) => (
                <th
                  key={index}
                  className="border-b border-white/10 px-4 py-3 text-center font-semibold"
                >
                  EMP {index + 1}
                </th>
              ))}
            </tr>
          </thead>

          <tbody>
            {previewRows.map((row, rowIndex) => (
              <tr key={rowIndex} className="odd:bg-white/[0.02]">
                <td className="border-b border-white/5 px-4 py-3 font-semibold text-teal-300">
                  TASK {rowIndex + 1}
                </td>

                {row.slice(0, maxPreview).map((value, colIndex) => (
                  <td
                    key={colIndex}
                    className="border-b border-white/5 px-4 py-3 text-center text-slate-200"
                  >
                    <span className="inline-flex min-w-[56px] items-center justify-center rounded-xl bg-white/5 px-3 py-1">
                      ${value}
                    </span>
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {matrix.length > maxPreview && (
        <div className="border-t border-white/10 px-5 py-3 text-xs text-slate-500">
          Only the first 10 rows and 10 columns are shown here.
        </div>
      )}
    </div>
  );
}