/*
 * Copyright 2020 WinT 3794 (Manuel Díaz Rojo and Alexis Obed García Hernández)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package LibTMOA.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PathReader {
    private JSONParser parser;
    private JSONArray path;

    private final String file;

    public PathReader(String file){
        this.parser = new JSONParser();
        this.file = file;

        parseObject();
    }

    private void parseObject(){
        try (FileReader reader = new FileReader(this.file))
        {
            Object obj = parser.parse(reader);

            JSONArray path = (JSONArray) obj;
            this.path = path;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getRawPath(){
        return this.path;
    }

    public Object getInitialPosition(){
        return this.path.stream().findFirst().get();
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
