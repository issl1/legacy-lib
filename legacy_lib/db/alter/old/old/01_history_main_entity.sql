UPDATE ts_main_entity SET mai_ent_is_custom_history = false;

UPDATE ts_main_entity SET mai_ent_is_custom_history = true WHERE ref_code = 'com.nokor.efinance.auction.model.Auction';
UPDATE ts_main_entity SET mai_ent_is_custom_history = true WHERE ref_code = 'com.nokor.efinance.core.contract.model.Contract';
UPDATE ts_main_entity SET mai_ent_is_custom_history = true WHERE ref_code = 'com.nokor.efinance.collection.model.Collection';
UPDATE ts_main_entity SET mai_ent_is_custom_history = true WHERE ref_code = 'com.nokor.efinance.core.payment.model.Payment';