
export class PasswordConfiguration {
  constructor(
    private _id: string,
    private _website: string,
    private _username: string,
    private _version: number,
    private _length: number
  ) {
  }

  get id(): string {
    return this._id;
  }

  get website(): string {
    return this._website;
  }

  get username(): string {
    return this._username;
  }

  get version(): number {
    return this._version;
  }

  get length(): number {
    return this._length;
  }
}
