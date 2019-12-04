# Porting LaCasa to Dotty

## Notes

DivideZero plugin doesn't compile with latest dotty.
dotty.tools.dotc.ast.Positioned.pos was renamed span in commit `616bc851a0ff7b097be9ba07e3e65972bd39eb72` (release 0.13.0-RC1)

### Tests
I've spent several hours now, just trying to understand how
Dottys tests work. Most test frameworks seem to work poorly with Dotty.
Understanding how to just have a simple test case that compiles
some file with a compiler plugin enabled is not easy.

I also don't understand at all how, when or if Dottys own tests
test their DivideZero compile plugin.
