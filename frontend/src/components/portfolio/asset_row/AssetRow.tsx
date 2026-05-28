interface AssetRowProps {
  assetName: string;
}
export default function AssetRow({ assetName }: AssetRowProps) {
  return <li className="element-item">Hello from {assetName}!</li>;
}
