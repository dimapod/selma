/*
 * Copyright 2013 Xebia and Séven Le Mesle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;
import fr.xebia.extras.selma.it.mappers.MissingPropertyMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 *
 */
@Compile(withClasses = MissingPropertyMapper.class, shouldFail = true)
public class FailingMappersIT extends IntegrationTestBase {



    @Test
    public void compilation_should_fail_on_missing_out_property_without_ignore() throws Exception {

        assertCompilationError(PersonIn.class, "public boolean isMale() {", String.format("getter for field Male from in bean %s is missing in destination bean %s", PersonIn.class.getName(), PersonOut.class.getName()));

    }

    @Test
    public void compilation_should_fail_on_missing_in_property_without_ignore() throws Exception {

        assertCompilationError(PersonOut.class, "public void setBiography(String biography) {", String.format("setter for field Biography from out bean %s has no getter in in bean %s", PersonOut.class.getName(), PersonIn.class.getName()));

    }



}
