interface AssetRowProps {
  assetName: string;
}
export default function AssetRow({ assetName }: AssetRowProps) {
  async function sellAsset() {}

  async function buyAsset() {}
  return (
    <li className="element-item">
      Hello from {assetName}!{" "}
      <button className="btn-primary" onClick={buyAsset}>
        Buy
      </button>
      <button className="btn-primary" onClick={sellAsset}>
        Sell
      </button>
    </li>
  );
}
