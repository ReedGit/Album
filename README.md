##Album
--

An Android library to open album for selecting pictures
##Geting Started
--

In your build.gradle:
```
dependencies {
    compile 'me.reed.album:album:0.1.0'
}
```
##Usage
--

When your want to open album for selecting pictures, follow the code:
```
AlbumUtil.getInstance().openAlbum(MainActivity.this,6, new AlbumUtil.AlbumCallback() {
                    @Override
                    public void onResult(List<String> paths) {

                    }
                });
```
##License
--
```
Copyright 2016 Reed.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```