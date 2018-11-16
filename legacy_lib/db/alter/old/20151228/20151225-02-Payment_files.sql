UPDATE tu_asset_model
   SET ass_mod_code = 'ASS' || ass_mod_id
 WHERE ass_mod_code is null or ass_mod_code = '';
