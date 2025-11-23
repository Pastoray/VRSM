package pkg.users;

/**
 * Defines the type of customer for specialized handling (e.g., pricing, terms).
 * Uses EnumType.STRING to persist the literal name of the enum value.
 */
enum CustomerType
{
    INDIVIDUAL,
    COMPANY
}
