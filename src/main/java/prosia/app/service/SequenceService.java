/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.service;

import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.app.model.Sequence;
import prosia.app.model.SequenceInstance;
import prosia.app.repo.SequenceRepo;

/**
 *
 * @author Randy
 */
@Service
@Transactional(readOnly = false, rollbackFor = { Exception.class })
public class SequenceService {
    
    @Autowired
    private SequenceRepo sequenceRepo;
    
    public String getNextValue(String sequenceName, HashMap<String, String> params) throws Exception {
        Sequence sequence = sequenceRepo.findTop1ByNameAndEnabled(sequenceName, true);
        
        if (sequence != null && sequence.getFormat() != null) {
            Integer nextNumber = 1;
            
            // generate prefix from format
            String prefix = sequence.getFormat();
            for (String key : params.keySet()) {
                prefix = prefix.replaceAll("<".concat(key).concat(">"), params.get(key));
            }
            
            // get instance
            SequenceInstance instance = sequence.getInstanceByPrefixSuffix(prefix);
            if (instance != null) {
                nextNumber = instance.getNextNumber();
                instance.setNextNumber(instance.getNextNumber() + 1);
            } else {
                sequence.addInstance(prefix, nextNumber + 1);
            }
            
            // update next_number on sequence
            sequenceRepo.save(sequence);
            
            return prefix.replaceAll("<next_number>", 
                    sequence.getSeqLength() != null && sequence.getSeqLength() > 0 
                            ? StringUtils.leftPad(String.valueOf(nextNumber), sequence.getSeqLength(), "0") 
                            : String.valueOf(nextNumber));
        }
        return null;
    }
}
