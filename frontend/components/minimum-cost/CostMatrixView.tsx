'use client';
type Props = {
    matrix: number[][];
};

export default function CostMatrixView({ matrix }: Props) {
    const maxPreview = 10;
    const previewRows = matrix.slice(0, maxPreview);
    const previewCols = matrix[0]?.slice(0, maxPreview) ?? [];
    return (
        <div className="overflow-auto border rounded-2xl">
            <table className="min-w-full text-sm border-collapse">
                <thead>
                    <tr className="bg-slate-100">
                        <th className="border px-3 py-2">Task / Employee</th>
                        {previewCols.map((_, index) => (
                            <th key={index} className="border px-3 py-2">E{index + 1}</th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {previewRows.map((row, rowIndex) => (
                        <tr key={rowIndex}>
                            <td className="border px-3 py-2 font-semibold bgslate-50">T{rowIndex + 1}</td>
                            {row.slice(0, maxPreview).map((value, colIndex) => (
                                <td key={colIndex} className="border px-3 py-2 text-center">$
                                    {value}
                                </td>
                            ))}
                        </tr>
                    ))}
                </tbody>
            </table>
            {matrix.length > maxPreview && (
                <div className="p-3 text-xs text-slate-500">
                    Showing first 10 x 10 preview only.
                </div>
            )}
        </div>
    );
}
