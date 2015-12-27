/*
 * Copyright 2015 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortmocks.web.basis.model.user.service;

import com.fortmocks.core.basis.model.Service;
import com.fortmocks.core.basis.model.ServiceResult;
import com.fortmocks.core.basis.model.ServiceTask;
import com.fortmocks.core.basis.model.user.domain.Status;
import com.fortmocks.core.basis.model.user.dto.UserDto;
import com.fortmocks.core.basis.model.user.service.message.input.CreateUserInput;
import com.fortmocks.core.basis.model.user.service.message.output.CreateUserOutput;

import java.util.Date;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
@org.springframework.stereotype.Service
public class CreateUserService extends AbstractUserService implements Service<CreateUserInput, CreateUserOutput> {

    /**
     * The process message is responsible for processing an incoming serviceTask and generate
     * a response based on the incoming serviceTask input
     * @param serviceTask The serviceTask that will be processed by the service
     * @return A result based on the processed incoming serviceTask
     * @see ServiceTask
     * @see ServiceResult
     */
    @Override
    public ServiceResult<CreateUserOutput> process(final ServiceTask<CreateUserInput> serviceTask) {
        final CreateUserInput input = serviceTask.getInput();
        final UserDto user = input.getUser();

        final UserDto existingUser = findByUsername(user.getUsername());
        if(existingUser != null){
            throw new IllegalArgumentException("User with the username '" + user.getUsername() + "' already exists.");
        }


        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        user.setStatus(Status.ACTIVE);
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));

        final UserDto savedUser = save(user);
        final CreateUserOutput output = new CreateUserOutput();
        output.setSavedUser(savedUser);
        return createServiceResult(output);
    }
}
