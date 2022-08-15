INLINE_RUNTIME_CHUNK=false npm run build

pushd build/static/js

mv -f 2*.js vendor.conference-form.js
mv -f main*.js main.conference-form.js
mv -f runtime~main*.js runtime.conference-form.js

popd

serve -l 5001 build
