interface AssetRowProps {
  assetName: string;
}
export default function AssetRow({ assetName }: AssetRowProps) {
  return <h1>Hello from {assetName}!</h1>;
}
