CREATE TABLE IF NOT EXISTS "payment" (
  "id"                  VARCHAR(256),
  "card_number"         VARCHAR(256)  NOT NULL,
  "card_expiry_date"    VARCHAR(256)  NOT NULL,
  "card_cvc"            VARCHAR(256)  NOT NULL,
  "amount"              VARCHAR(256)  NOT NULL,
  "currency"            VARCHAR(256)  NOT NULL,
  "approval_code"       VARCHAR(256)  NOT NULL,

  CONSTRAINT pk PRIMARY KEY ("id")
);

CREATE INDEX "payment_id"   ON "payment" ("id");