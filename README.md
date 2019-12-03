# Porting LaCasa to Dotty

## Notes

DivideZero plugin doesn't compile with latest dotty.
dotty.tools.dotc.ast.Positioned.pos was renamed span in commit `616bc851a0ff7b097be9ba07e3e65972bd39eb72` (release 0.13.0-RC1)