--key:SENTENCES_TABLE_CREATION
CREATE COLUMN TABLE "SENTENCES" (
	"ID" BIGINT NOT NULL ,
	"LANG" NVARCHAR(2),
	"TEXT" NVARCHAR(2000),
	PRIMARY KEY ("ID")
)

--key:SENTENCES_SEQ_CREATION
CREATE sequence "SENTENCES_IDSEQ"

--key:SENTENCES_FULLTEXT_IDX
CREATE FULLTEXT INDEX "SENTENCES_TEXT_INDEX" ON "SENTENCES"("TEXT")
ASYNC TEXT ANALYSIS ON CONFIGURATION 'indexConfigVOC.xml'
LANGUAGE COLUMN "LANG"

--key:SENTENCES_FULLTEXT_IDX_FK
ALTER TABLE "$TA_SENTENCES_TEXT_INDEX"
ADD CONSTRAINT "TA_FK" FOREIGN KEY("ID") REFERENCES "SENTENCES"("ID")
ON DELETE CASCADE

