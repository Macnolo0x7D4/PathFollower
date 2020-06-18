/*
 * Copyright 2020 WinT 3794 (Manuel Diaz Rojo and Alexis Obed Garcia Hernandez)
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

package org.wint3794.pathfollower.io;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class PathReader {
    protected final JSONParser parser;
    protected JSONArray path;

    public PathReader(String file) {
        this.parser = new JSONParser();
        this.path = readFile(file);
    }

    protected PathReader() {
        this.parser = new JSONParser();
    }

    private JSONArray readFile(String file) {
        JSONArray read = null;
        try (FileReader reader = new FileReader(file)) {
            read = parseObject(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return read;
    }

    public JSONArray getRawPath() {
        return this.path;
    }

    public Object getInitialPosition() {
        return this.path.stream().findFirst().get();
    }

    private JSONArray parseObject(FileReader reader) throws IOException, ParseException {
        Object obj = parser.parse(reader);

        return (JSONArray) obj;
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
