// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage.pm;

import ripple.common.entity.AbstractMessage;
import ripple.common.storage.MessageService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class PMBasedMessageService implements MessageService {
    @Override
    public boolean newMessage(AbstractMessage message) {
        // TODO
        return false;
    }

    @Override
    public boolean exist(UUID messageUuid) {
        // TODO
        return false;
    }

    @Override
    public AbstractMessage getMessageByUuid(UUID messageUuid) {
        // TODO
        return null;
    }

    @Override
    public List<AbstractMessage> findMessages(String applicationName, String key) {
        // TODO
        return Collections.emptyList();
    }
}
