# InvisibleBlock
置いたブロックが一定時間後に見えなくなるプラグイン

## 動作環境
- Minecraft 1.16.5
- PaperMC 1.16.5

## コマンド

- Ib
    - start

      - ブロックの透明化開始

    - stop

      - ブロックの透明化を終了し、透明化されたブロックを可視化

    - add <playerName|@a|@r|@s|@p>

      - 指定したプレイヤーが置いたブロックが透明化されるようになる

    - remove <playerName|@a|@r|@s|@p>

      - add で追加したプレイヤーを削除

    - setTime <数字(秒)>

      - ブロックが透明化されるまでの時間を設定
      - デフォルトは5(秒)

    - setMaxRevertNum <数字>

      - ブロックの可視化時に1Tickあたりに可視化されるブロック数を指定
      - デフォルトは1000(Block/tick)

    - showStatus

      各種設定の設定値を出力
