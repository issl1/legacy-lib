--DEV and PRD
UPDATE td_cashflow SET cfw_bl_paid = true WHERE pay_id IS NOT NULL AND cfw_bl_paid = false;